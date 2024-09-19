package com.sta.xuptsta.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import com.sta.xuptsta.exception.GlobalException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    RateLimiter rateLimiter = RateLimiter.create(15);
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(rateLimiter.tryAcquire()){
            return true;
        }
        else{
            throw new GlobalException("请求限流，请稍后再试！");
        }
    }

}
