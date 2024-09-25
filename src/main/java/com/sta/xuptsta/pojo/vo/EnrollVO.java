package com.sta.xuptsta.pojo.vo;

import lombok.Data;
@Data
public class EnrollVO {
    private String number;
    private String name;
    private String majorClass;
    private String telephone;
    private String intention;
    private Integer firstTime; //一面时间
    private Integer secondTime; //二面时间
    private Integer status;
    private String message;
}
