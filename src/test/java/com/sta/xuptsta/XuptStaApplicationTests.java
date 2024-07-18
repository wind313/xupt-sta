package com.sta.xuptsta;

import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class XuptStaApplicationTests {
    @Autowired
    private EmailService emailService;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        redisTemplate.delete(EmailConstant.EMAIL_CODE + "3136002710@qq.com");
    }

}
