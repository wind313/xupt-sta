package com.sta.xuptsta.pojo.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserCodeDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 4, message = "验证码长度为四位")
    private String code;
}
