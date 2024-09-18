package com.sta.xuptsta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sta.xuptsta.pojo.dto.EnrollDTO;
import com.sta.xuptsta.pojo.entity.Enroll;
import com.sta.xuptsta.pojo.vo.EnrollVO;

public interface EnrollService extends IService<Enroll> {
    void add(EnrollDTO enrollDto);

    void modify(EnrollDTO enrollDto);

    EnrollVO get();

    void selectSecond(Long timeId);
}
