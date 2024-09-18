package com.sta.xuptsta.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("enroll")
public class Enroll {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String number;
    private String name;
    private String majorClass;
    private String telephone;
    private String intention;
    private Integer firstTime; //一面时间
    private Integer secondTime; //二面时间
    private Integer status; //0：已报名 1：一面通过 2：二面通过 3：未通过
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
