package com.sta.xuptsta.interceptor;

import com.sta.xuptsta.exception.GlobalException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final int RATE = 50;
    private static final int CAPACITY = 40;
    private volatile int curNum = 40;
    private volatile long timeStamp = System.currentTimeMillis();
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(curNum>0){
            curNum--;
            return true;
        }
        long now = System.currentTimeMillis();
        if(now - timeStamp >= RATE){
            if((now - timeStamp)/RATE > 1){
                curNum += (int)(now - timeStamp)/RATE - 1;
            }
            if(curNum > CAPACITY){
                curNum = CAPACITY;
            }
            timeStamp = now;
            return true;
        }
        throw new GlobalException("请求限流，请稍后再试");
    }

}
