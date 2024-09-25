package com.sta.xuptsta.pojo.dto;

import lombok.Getter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class EnrollDTO {
    @NotBlank(message = "学号不能为空")
    @Size(min = 2, max = 20, message = "学号格式错误")
    private String number;
    @Size(min = 2, max = 10, message = "姓名长度为2-10")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "专业班级不能为空")
    @Size(min = 6, max = 20, message = "班级格式错误")
    private String majorClass;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String telephone;
    @NotNull(message = "一面时间不能为空")
    private Integer firstTime;
    @NotBlank(message = "意向方向不能为空")
    @Size(min = 2, max = 4, message = "意向方向格式错误")
    private String intention; //
}
