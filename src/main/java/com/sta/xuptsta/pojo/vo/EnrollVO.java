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
    private Integer status; //0：已报名 1：一面通过 2：二面通过 3：未通过
}
