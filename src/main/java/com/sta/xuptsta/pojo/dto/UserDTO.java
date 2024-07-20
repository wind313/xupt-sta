package com.sta.xuptsta.pojo.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank(message = "邮箱不能为空")
    private String email;
    private String code;
    private String password;
}
