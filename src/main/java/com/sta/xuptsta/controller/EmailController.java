package com.sta.xuptsta.controller;

import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/email")
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;
    @PostMapping("/register")
    public Result sendRegisterCode(@RequestParam("email") String email)
    {
        emailService.sendRegisterCode(email);
        return Result.ok();
    }
    @PostMapping("/login")
    public Result sendLoginCode(@RequestParam("email") String email)
    {
        emailService.sendRegisterCode(email);
        return Result.ok();
    }
    @PostMapping("/password")
    public Result sendPasswordCode(@RequestParam("email") String email)
    {
        emailService.sendRegisterCode(email);
        return Result.ok();
    }
}
