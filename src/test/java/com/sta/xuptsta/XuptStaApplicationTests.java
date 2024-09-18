package com.sta.xuptsta;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.holder.CurrentUserIdHolder;
import com.sta.xuptsta.pojo.entity.User;
import com.sta.xuptsta.service.EmailService;
import com.sta.xuptsta.service.UserService;
import com.sta.xuptsta.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
class XuptStaApplicationTests {
    @Autowired
    private EmailService emailService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        List<User> list = userService.list();
        for(User user:list){
            if(user.getId()>2){
                String s = MD5Util.md5(user.getPassword());
                user.setPassword(s);
            }
        }
        userService.updateBatchById(list);
    }

}
