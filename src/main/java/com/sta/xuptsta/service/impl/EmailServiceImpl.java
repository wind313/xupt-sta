package com.sta.xuptsta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.mq.EmailProvider;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.service.EmailService;
import com.sta.xuptsta.service.UserService;
import com.sta.xuptsta.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final String REGISTER_HTML = "registerEmail";
    private final String LOGIN_HTML = "loginEmail";
    private final String PASSWORD_HTML = "passwordEmail";

    @Autowired
    private UserService userService;

    @Autowired
    private EmailProvider emailProvider;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public void sendRegisterCode(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) > 0) {
            throw new GlobalException("用户已注册，请登录！");
        }

        String code = emailUtil.getCode();
        String content = emailUtil.getContent(code, REGISTER_HTML);

        emailProvider.sendEmail(email,code,content);
    }

    @Override
    public void sendLoginCode(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) == 0) {
            throw new GlobalException("用户不存在，请注册！");
        }

        String code = emailUtil.getCode();
        String content = emailUtil.getContent(code, LOGIN_HTML);
        
        emailProvider.sendEmail(email,code,content);
    }

    @Override
    public void sendPasswordCode(String email) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) == 0) {
            throw new GlobalException("用户不存在，请注册！");
        }

        String code = emailUtil.getCode();
        String content = emailUtil.getContent(code, PASSWORD_HTML);

        emailProvider.sendEmail(email,code,content);
    }
}
