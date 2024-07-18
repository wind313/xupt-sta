package com.sta.xuptsta;

import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.holder.CurrentUserIdHolder;
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
        CurrentUserIdHolder.removeCurrentUserId();
    }

}
