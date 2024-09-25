package com.sta.xuptsta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sta.xuptsta.enums.EnrollStatusCode;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.holder.CurrentUserIdHolder;
import com.sta.xuptsta.mapper.EnrollMapper;
import com.sta.xuptsta.pojo.dto.EnrollDTO;
import com.sta.xuptsta.pojo.entity.Enroll;
import com.sta.xuptsta.pojo.entity.InterviewTime;
import com.sta.xuptsta.pojo.vo.EnrollVO;
import com.sta.xuptsta.service.EnrollService;
import com.sta.xuptsta.service.InterviewTimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class EnrollServiceImpl extends ServiceImpl<EnrollMapper, Enroll> implements EnrollService {

    @Autowired
    private InterviewTimeService interviewTimeService;

    private static final int timeSlotMaxCapacity = 30;//每个时段最大报名人数
    private static final int MAX_RETRY_COUNT = 2;//重试机制有自旋问题，影响性能，不能太大

    @Override
    @Transactional
    public void add(EnrollDTO enrollDto) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        //判断是否报名
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        if (this.count(queryWrapper) > 0) {
            throw new GlobalException("你已报名！");
        }
        //重试机制
        int retryCount = 0;
        while(retryCount < MAX_RETRY_COUNT){
            InterviewTime interviewTime = interviewTimeService.getById(enrollDto.getFirstTime());
            Integer count = interviewTime.getCount();
            //判断面试时间是否可选
            if (interviewTime.getStartTime().isBefore(LocalDateTime.now())){
                throw new GlobalException("该面试时间已过，不能选择！");
            }
            if(count >= timeSlotMaxCapacity){
                throw new GlobalException("该时间段人数已达上限！");
            }

            interviewTime.setCount(count +1);
            //更新该面试时间报名人数（乐观锁）
            if(interviewTimeService.updateById(interviewTime)) {
                Enroll enroll = new Enroll();
                BeanUtils.copyProperties(enrollDto, enroll);
                enroll.setUserId(userId);
                enroll.setStatus(EnrollStatusCode.ENROLLED.getCode());
                //保存报名信息
                this.save(enroll);
                return;
            }
            retryCount++;
        }
        //由于并发问题更新失败
        throw new GlobalException("系统繁忙，请重试！");
    }

    @Override
    @Transactional
    public void modify(EnrollDTO enrollDto) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        //判断是否报名
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        Enroll enroll = this.getOne(queryWrapper);
        if(enroll == null){
            throw new GlobalException("未报名！");
        }
        //重试机制
        int retryCount = 0;
        boolean oldSuccess = false;
        boolean newSuccess = false;
        while(retryCount < MAX_RETRY_COUNT){
            InterviewTime newTime = interviewTimeService.getById(enrollDto.getFirstTime());
            InterviewTime oldTime = interviewTimeService.getById(enroll.getFirstTime());
            //判断面试时间是否变化
            if(!oldTime.getId().equals(newTime.getId())){
                //更新面试时间报名人数（乐观锁）
                if(!oldSuccess){
                    if (oldTime.getStartTime().isBefore(LocalDateTime.now())){
                        throw new GlobalException("你的面试时间已过，不能修改！");
                    }
                    oldTime.setCount(oldTime.getCount()-1);
                    oldSuccess = interviewTimeService.updateById(oldTime);
                }
                if(!newSuccess){
                    if (newTime.getStartTime().isBefore(LocalDateTime.now())){
                        throw new GlobalException("该面试时间已过，不能选择！");
                    }
                    if(newTime.getCount() >= timeSlotMaxCapacity){
                        throw new GlobalException("该时间段人数以达上限！");
                    }
                    newTime.setCount(newTime.getCount()+1);
                    newSuccess = interviewTimeService.updateById(newTime);
                }
                if(!oldSuccess || !newSuccess){
                    retryCount++;
                    continue;
                }
            }

            UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Enroll::getUserId, userId)
                    .set(Enroll::getNumber, enrollDto.getNumber())
                    .set(Enroll::getName, enrollDto.getName())
                    .set(Enroll::getMajorClass, enrollDto.getMajorClass())
                    .set(Enroll::getTelephone, enrollDto.getTelephone())
                    .set(Enroll::getIntention, enrollDto.getIntention())
                    .set(Enroll::getFirstTime, enrollDto.getFirstTime())
                    .set(Enroll::getUpdateTime, LocalDateTime.now());
            //更新报名信息
            this.update(updateWrapper);
            return;
        }
        //由于并发问题更新失败
        throw new GlobalException("系统繁忙，请重试！");
    }

    @Override
    public EnrollVO get() {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        EnrollVO enrollVO = new EnrollVO();
        Enroll enroll = this.getOne(queryWrapper);
        if(enroll == null){
            return null;
        }
        BeanUtils.copyProperties(enroll, enrollVO);
        switch (enroll.getStatus()){
            case 0:
                enrollVO.setStatus(EnrollStatusCode.UN_ENROLLED.getCode());
                enrollVO.setMessage(EnrollStatusCode.UN_ENROLLED.getMessage());
                break;
            case 1:
                enrollVO.setStatus(EnrollStatusCode.ENROLLED.getCode());
                enrollVO.setMessage(EnrollStatusCode.ENROLLED.getMessage());
                break;
            case 2:
                enrollVO.setStatus(EnrollStatusCode.FIRST_PASS.getCode());
                enrollVO.setMessage(EnrollStatusCode.FIRST_PASS.getMessage());
                break;
            case 3:
                enrollVO.setStatus(EnrollStatusCode.SECOND_PASS.getCode());
                enrollVO.setMessage(EnrollStatusCode.SECOND_PASS.getMessage());
               break;
            case 4:
                enrollVO.setStatus(EnrollStatusCode.FIRST_Fail.getCode());
                enrollVO.setMessage(EnrollStatusCode.FIRST_Fail.getMessage());
                break;
            case 5:
                enrollVO.setStatus(EnrollStatusCode.SECOND_FAIL.getCode());
                enrollVO.setMessage(EnrollStatusCode.SECOND_FAIL.getMessage());
                break;

        }
        return enrollVO;
    }

    @Override
    @Transactional
    public void selectSecond(Long timeId) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        //判断是否通过一面
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        Enroll enroll = this.getOne(queryWrapper);
        if(!enroll.getStatus().equals(EnrollStatusCode.FIRST_PASS.getCode()) && !enroll.getStatus().equals(EnrollStatusCode.SECOND_PASS.getCode())){
            throw new GlobalException("未通过一面！");
        }
        //重试机制
        int retryCount = 0;
        boolean oldSuccess = false;
        boolean newSuccess = false;
        Integer oldTime = enroll.getSecondTime();
        if(oldTime.equals(0)) oldSuccess = true;
        while(retryCount < MAX_RETRY_COUNT){
            //更新面试时间报名人数（乐观锁）
            if(!oldSuccess){
                InterviewTime time = interviewTimeService.getById(oldTime);
                if(!oldTime.equals(0)){
                    if (time != null && time.getStartTime().isBefore(LocalDateTime.now())){
                        throw new GlobalException("你的面试时间已过，不能更改！");
                    }
                }
                time.setCount(time.getCount()-1);
                oldSuccess = interviewTimeService.updateById(time);
            }
            if(!newSuccess){
                InterviewTime newTime = interviewTimeService.getById(timeId);
                if(newTime.getStartTime().isBefore(LocalDateTime.now())){
                    throw new GlobalException("该面试时间已过，不能选择！");
                }
                if(newTime.getCount() >= timeSlotMaxCapacity){
                    throw new GlobalException("该时间段人数已达上限！");
                }
                newTime.setCount(newTime.getCount()+1);
                newSuccess = interviewTimeService.updateById(newTime);
            }
            if(oldSuccess && newSuccess){
                //更新报名信息
                UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(Enroll::getUserId, userId)
                        .set(Enroll::getSecondTime, timeId)
                        .set(Enroll::getUpdateTime, LocalDateTime.now());
                this.update(updateWrapper);
                return;
            }
            retryCount++;
        }
        //由于并发问题更新失败
        throw new GlobalException("系统繁忙，请重试！");
    }
}
