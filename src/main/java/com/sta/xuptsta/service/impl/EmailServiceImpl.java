package com.sta.xuptsta.service.impl;

import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    private final String SUBJECT = "西邮软件科技协会";
    private final String REGISTER_PREFIX = "    感谢您对西邮软件科技协会的兴趣！为了确保您的账户安全，我们需要验证您的电子邮件地址。如果您没有请求此验证码，请忽略此邮件。\n" +
            "    请在接下来的15分钟内使用以下验证码完成注册过程：";
    private final String LOGIN_PREFIX = "    欢迎访问西邮软件科技协会官方网站！为了确保您的账户安全，我们需要验证您的电子邮件地址。如果您没有请求此验证码，请忽略此邮件。\n" +
            "    请在接下来的15分钟内使用以下验证码完成登录过程：";
    private final String PASSWORD_PREFIX = "    为了您的账户安全，我们需要验证您的电子邮件地址。如果您没有请求此验证码，请忽略此邮件。\n" +
            "    请在接下来的15分钟内使用以下验证码完成修改密码过程：";

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String to, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(content);
        javaMailSender.send(mailMessage);
    }

    private String getCode() {
        int code = (int) (Math.random() * 10000);
        return String.format("%04d", code);
    }

    @Override
    public void sendRegisterCode(String email) {
        String code = getCode();
        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, 15, TimeUnit.MINUTES);
            sendEmail(email, REGISTER_PREFIX + code);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }

    @Override
    public void sendLoginCode(String email) {
        String code = getCode();
        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, 15, TimeUnit.MINUTES);
            sendEmail(email, LOGIN_PREFIX + code);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }

    @Override
    public void sendPasswordCode(String email) {
        String code = getCode();
        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + email, code, 15, TimeUnit.MINUTES);
            sendEmail(email, PASSWORD_PREFIX + code);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }
    }

}
