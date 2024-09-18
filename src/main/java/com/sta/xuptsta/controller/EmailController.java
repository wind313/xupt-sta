package com.sta.xuptsta.controller;

import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "邮件")
@RequestMapping("/email")
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "注册邮件", description = "发送注册的验证码邮件")
    @PostMapping("/register")
    public Result sendRegisterCode(@RequestParam("email") String email)
    {
        emailService.sendRegisterCode(email);
        return Result.ok();
    }

    @Operation(summary = "登录邮件", description = "发送登录的验证码邮件")
    @PostMapping("/login")
    public Result sendLoginCode(@RequestParam("email") String email)
    {
        emailService.sendLoginCode(email);
        return Result.ok();
    }

    @Operation(summary = "密码邮件", description = "发送修改密码的验证码邮件")
    @PostMapping("/password")
    public Result sendPasswordCode(@RequestParam("email") String email)
    {
        emailService.sendPasswordCode(email);
        return Result.ok();
    }
}
