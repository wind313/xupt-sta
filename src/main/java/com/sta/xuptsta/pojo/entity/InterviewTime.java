package com.sta.xuptsta.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("interview_time")
public class InterviewTime {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer interviewType;//1：一面 2：二面
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer count;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @Version
    private Integer version;

}
