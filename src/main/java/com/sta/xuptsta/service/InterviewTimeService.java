package com.sta.xuptsta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sta.xuptsta.pojo.entity.InterviewTime;
import com.sta.xuptsta.pojo.vo.InterviewTimeVO;

import java.util.List;

public interface InterviewTimeService extends IService<InterviewTime> {
    List<InterviewTimeVO> get(Integer type);
}
