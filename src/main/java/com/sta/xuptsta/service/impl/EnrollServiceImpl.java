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

    private final int timeSlotMaxCapacity = 30;

    @Override
    @Transactional
    public void add(EnrollDTO enrollDto) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId).or().eq(Enroll::getNumber, enrollDto.getNumber());
        if (this.count(queryWrapper) > 0) {
            throw new GlobalException("你已报名！");
        }
        InterviewTime interviewTime = interviewTimeService.getById(enrollDto.getFirstTime());
        Integer count = interviewTime.getCount();
        if(count >= timeSlotMaxCapacity){
            throw new GlobalException("该时间段人数已达上限！");
        }

        interviewTime.setCount(count +1);
        Enroll enroll = new Enroll();
        BeanUtils.copyProperties(enrollDto, enroll);
        enroll.setUserId(userId);
        enroll.setStatus(0);

        if(!interviewTimeService.updateById(interviewTime)) {
            throw new GlobalException("系统繁忙，请重试！");
        }
        this.save(enroll);
    }

    @Override
    @Transactional
    public void modify(EnrollDTO enrollDto) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();

        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getNumber, enrollDto.getNumber());
        Enroll enroll = this.getOne(queryWrapper);
        if (!enroll.getUserId().equals(userId)) {
            throw new GlobalException("该学号已被其他用户报名！");
        }

        InterviewTime newTime = interviewTimeService.getById(enrollDto.getFirstTime());
        InterviewTime oldTime = interviewTimeService.getById(enroll.getFirstTime());
        if(!oldTime.getId().equals(newTime.getId())){
            if (newTime.getStartTime().isBefore(LocalDateTime.now())){
                throw new GlobalException("该面试时间已过，不能选择！");
            }
            if(newTime.getCount() >= timeSlotMaxCapacity){
                throw new GlobalException("该时间段人数以达上限！");
            }
            if (oldTime.getStartTime().isBefore(LocalDateTime.now())){
                throw new GlobalException("你的面试时间已过，不能修改！");
            }
            newTime.setCount(newTime.getCount()+1);
            oldTime.setCount(oldTime.getCount()-1);
            if(!interviewTimeService.updateById(oldTime) || !interviewTimeService.updateById(newTime)){
                throw new GlobalException("系统繁忙，请重试！");
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
        this.update(updateWrapper);
    }

    @Override
    public EnrollVO get() {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        EnrollVO enrollVO = new EnrollVO();
        Enroll enroll = this.getOne(queryWrapper);
        if(enroll == null){
            throw new GlobalException("未报名！");
        }
        BeanUtils.copyProperties(enroll, enrollVO);
        return enrollVO;
    }

    @Override
    @Transactional
    public void selectSecond(Long timeId) {
        Long userId = CurrentUserIdHolder.getCurrentUserId();
        InterviewTime newTime = interviewTimeService.getById(timeId);
        if(newTime.getStartTime().isBefore(LocalDateTime.now())){
            throw new GlobalException("该面试时间已过，不能选择！");
        }
        if(newTime.getCount() >= timeSlotMaxCapacity){
            throw new GlobalException("该时间段人数已达上限！");
        }

        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Enroll::getUserId, userId);
        Enroll enroll = this.getOne(queryWrapper);

        if(enroll.getStatus().equals(EnrollStatusCode.ENROLLED.getCode()) || enroll.getStatus().equals(EnrollStatusCode.Fail.getCode())){
            throw new GlobalException("未通过一面！");
        }

        Integer oldTime = enroll.getSecondTime();
        InterviewTime time = interviewTimeService.getById(oldTime);
        if(!oldTime.equals(0)){
            if (time != null && time.getStartTime().isBefore(LocalDateTime.now())){
                throw new GlobalException("你的面试时间已过，不能更改！");
            }
        }

        time.setCount(time.getCount()-1);
        newTime.setCount(time.getCount()+1);
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Enroll::getUserId, userId)
                .set(Enroll::getSecondTime, timeId)
                .set(Enroll::getUpdateTime, LocalDateTime.now());

        if(!interviewTimeService.updateById(time) || !interviewTimeService.updateById(newTime)){
            throw new GlobalException("系统繁忙，请重试！");
        }
        this.update(updateWrapper);
    }
}
