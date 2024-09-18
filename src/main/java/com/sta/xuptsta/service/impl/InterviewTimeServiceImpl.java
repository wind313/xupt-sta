package com.sta.xuptsta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sta.xuptsta.mapper.InterviewTimeMapper;
import com.sta.xuptsta.pojo.entity.InterviewTime;
import com.sta.xuptsta.pojo.vo.InterviewTimeVO;
import com.sta.xuptsta.service.InterviewTimeService;
import com.sta.xuptsta.util.LocalDateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewTimeServiceImpl extends ServiceImpl<InterviewTimeMapper, InterviewTime> implements InterviewTimeService {

    @Override
    public List<InterviewTimeVO> get(Integer type) {

        QueryWrapper<InterviewTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterviewTime::getInterviewType, type)
                .gt(InterviewTime::getStartTime, System.currentTimeMillis())
                .orderByAsc(InterviewTime::getStartTime);
        List<InterviewTime> list = this.list(queryWrapper);
        List<InterviewTimeVO> collect = list.stream().map(time -> {
            InterviewTimeVO timeVO = new InterviewTimeVO();
            timeVO.setId(time.getId());
            timeVO.setStartTime(LocalDateTimeFormatter.dateTimeToString(time.getStartTime()));
            timeVO.setEndTime(LocalDateTimeFormatter.dateTimeToString(time.getEndTime()));
            return timeVO;
        }).collect(Collectors.toList());

        return collect;
    }
}
