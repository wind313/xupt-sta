package com.sta.xuptsta.pojo.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class EnrollDTO {
    @NotBlank(message = "学号不能为空")
    @Length(min = 8, max = 8, message = "学号长度为8位")
    private String number;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "专业班级不能为空")
    private String majorClass;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String telephone;
    @NotNull(message = "一面时间不能为空")
    private Integer firstTime;
    @NotNull(message = "意向方向不能为空")
    private String intention; //0：不知道 1：Java 2：Go 3：前端 4：C++
}
