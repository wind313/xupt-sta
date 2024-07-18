package com.sta.xuptsta.interceptor;

import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.holder.CurrentUserIdHolder;
import com.sta.xuptsta.util.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sta.xuptsta.result.ResultCode;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if(token == null){
            throw new GlobalException(ResultCode.NOT_LOGIN);
        }
        if(!JWTUtil.checkToken(token)){
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        CurrentUserIdHolder.setCurrentUserId(JWTUtil.getUserId(token));
        return true;
    }
}
