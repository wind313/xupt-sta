package com.sta.xuptsta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.service.EmailService;
import com.sta.xuptsta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    private final String SUBJECT = "西邮软件科技协会";
    private final String REGISTER_HTML = "registerEmail";
    private final String LOGIN_HTML = "loginEmail";
    private final String PASSWORD_HTML = "passwordEmail";



    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private  JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    private void sendCodeEmail(String to, String content) throws MessagingException {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        mimeMessageHelper.setSubject(SUBJECT);
        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setFrom(from);
        javaMailSender.send(mailMessage);
    }

    private String getCode() {
        int code = (int) (Math.random() * 10000);
        return String.format("%04d", code);
    }
    private String getContent(String code, String name) {
        Context context = new Context();
        context.setVariable("code",code);
        return templateEngine.process(name, context);
    }

    @Override
    public void sendRegisterCode(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) > 0) {
            throw new GlobalException("用户已注册，请登录！");
        }

        String code = getCode();
        String content = getContent(code, REGISTER_HTML);

        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, EmailConstant.EMAIL_CODE_EXPIRE, TimeUnit.SECONDS);
            sendCodeEmail(email, content);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }

    @Override
    public void sendLoginCode(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) == 0) {
            throw new GlobalException("用户不存在，请注册！");
        }

        String code = getCode();
        String content = getContent(code, LOGIN_HTML);

        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, EmailConstant.EMAIL_CODE_EXPIRE, TimeUnit.SECONDS);
            sendCodeEmail(email,content);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }

    @Override
    public void sendPasswordCode(String email) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getEmail, email);
        if (userService.count(userQueryWrapper) == 0) {
            throw new GlobalException("用户不存在，请注册！");
        }

        String code = getCode();
        String content = getContent(code, PASSWORD_HTML);

        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, EmailConstant.EMAIL_CODE_EXPIRE, TimeUnit.SECONDS);
            sendCodeEmail(email, content);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }





}
