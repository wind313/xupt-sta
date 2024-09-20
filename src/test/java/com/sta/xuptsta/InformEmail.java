package com.sta.xuptsta;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sta.xuptsta.enums.EnrollStatusCode;
import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.pojo.entity.Enroll;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.service.EnrollService;
import com.sta.xuptsta.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class InformEmail {

    @Autowired
    UserService userService;
    @Autowired
    EnrollService enrollService;

    private final String SUBJECT = "西邮软件科技协会";
    private final String firstEmailName = "firstInform";
    private final String secondEmailName = "secondInform";
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    @Test
    public void sendFirst(){
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.lambda().eq(Enroll::getStatus, EnrollStatusCode.FIRST_PASS.getCode());
        List<Enroll> enrols = enrollService.list(enrollQueryWrapper);
        List<Long> ids = enrols.stream().map(enroll -> enroll.getUserId()).collect(Collectors.toList());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().in(User::getId,ids);
        List<User> users = userService.list(userQueryWrapper);

        List<Student> students = users.stream().map(user -> {
            Student student = new Student();
            student.setEmail(user.getEmail());
            Enroll enroll1 = enrols.stream().filter(enroll -> enroll.getUserId().equals(user.getId())).findFirst().get();
            student.setName(enroll1.getName());
            return student;
        }).collect(Collectors.toList());

        for(Student student:students){

            try {
                Context context = new Context();
                context.setVariable("name",student.getName());
                String content = templateEngine.process(firstEmailName, context);

                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
                mimeMessageHelper.setSubject(SUBJECT);
                mimeMessageHelper.setText(content, true);
                mimeMessageHelper.setTo(student.getEmail());
                mimeMessageHelper.setFrom(from);
                javaMailSender.send(mailMessage);
                log.warn(student.getName()+" "+student.getEmail()+"发送成功!");

            } catch (Exception e) {
                throw new GlobalException(student.getName()+" "+student.getEmail()+"发送失败!");
            }

        }
    }

    @Test
    public void sendSecond(){
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.lambda().eq(Enroll::getStatus, EnrollStatusCode.SECOND_PASS.getCode());
        List<Enroll> enrols = enrollService.list(enrollQueryWrapper);
        List<Long> ids = enrols.stream().map(enroll -> enroll.getUserId()).collect(Collectors.toList());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().in(User::getId,ids);
        List<User> users = userService.list(userQueryWrapper);

        List<Student> students = users.stream().map(user -> {
            Student student = new Student();
            student.setEmail(user.getEmail());
            Enroll enroll1 = enrols.stream().filter(enroll -> enroll.getUserId().equals(user.getId())).findFirst().get();
            student.setName(enroll1.getName());
            return student;
        }).collect(Collectors.toList());

        for(Student student:students){

            try {
                Context context = new Context();
                context.setVariable("name",student.getName());
                String content = templateEngine.process(secondEmailName, context);

                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
                mimeMessageHelper.setSubject(SUBJECT);
                mimeMessageHelper.setText(content, true);
                mimeMessageHelper.setTo(student.getEmail());
                mimeMessageHelper.setFrom(from);
                javaMailSender.send(mailMessage);
                Thread.sleep(6000);

            } catch (Exception e) {
                throw new GlobalException(student.getName()+" "+student.getEmail()+"发送失败!");
            }

        }
    }
}
