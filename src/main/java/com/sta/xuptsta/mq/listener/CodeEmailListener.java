package com.sta.xuptsta.mq.listener;

import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.mq.model.CodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RabbitListener(queues = "xupt-sta-1")
public class CodeEmailListener {

    private final String SUBJECT = "西邮软件科技协会";
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitHandler
    public void process(CodeMessage codeMessage) {
        try {
            redisTemplate.opsForValue().set(EmailConstant.EMAIL_CODE + codeMessage.getEmail(), codeMessage.getCode(), EmailConstant.EMAIL_CODE_EXPIRE, TimeUnit.SECONDS);
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setText(codeMessage.getContent(), true);
            mimeMessageHelper.setTo(codeMessage.getEmail());
            mimeMessageHelper.setFrom(from);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new GlobalException("发送邮件失败");
        }

        //监控每日邮件发送数量
        redisTemplate.opsForValue().increment("daily_email_count", 1);
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay();
        // 计算从现在到次日零点的秒数
        long secondsUntilMidnight = java.time.Duration.between(now, nextMidnight).getSeconds();
        // 设置过期时间为次日零点
        redisTemplate.expire("daily_email_count", secondsUntilMidnight, TimeUnit.SECONDS);
    }
}
