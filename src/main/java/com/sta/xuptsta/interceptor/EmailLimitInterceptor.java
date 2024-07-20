package com.sta.xuptsta.interceptor;

import com.sta.xuptsta.constant.EmailConstant;
import com.sta.xuptsta.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 实现HandlerInterceptor接口，用于拦截邮箱验证码的发送请求，以限制发送频率。
 */
@Component
public class EmailLimitInterceptor implements HandlerInterceptor {
    private final String EMAIL_PATTERN = "^(?:(?:[^<>()\\[\\]\\\\.,;:\\s@\"]+(?:\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(?:<[^<>()\\[\\]\\\\.,;:\\s@\"]+>))@(?:(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * 自动注入StringRedisTemplate，用于操作Redis缓存。
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 频率限制的时间间隔，单位为秒。
     */
    private final long LIMIT_TIME = 60L;

    /**
     * 在请求处理之前进行预处理，检查邮箱验证码的发送频率是否超过限制。
     *
     * @param request  HttpServletRequest对象，用于获取请求参数。
     * @param response HttpServletResponse对象，可用于设置响应状态码或头信息。
     * @param handler  将要执行的处理器对象。
     * @return 如果允许继续处理请求，返回true；否则返回false。
     * @throws Exception 如果预处理过程中抛出异常。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求参数中获取邮箱地址
        String email = request.getParameter("email");
        // 验证邮箱格式
        if (!pattern.matcher(email).matches()){
            throw new GlobalException("邮箱格式错误");
        }
        // 构建邮箱验证码的Redis键
        String key = EmailConstant.EMAIL_CODE + email;
        // 获取键的过期时间，单位为秒
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        // 如果键存在且过期时间大于预设的频率限制时间，则抛出异常提示发送太频繁
        if(expire != null && expire > EmailConstant.EMAIL_CODE_EXPIRE-LIMIT_TIME){
            throw new GlobalException("发送太频繁，请稍后再试");
        }
        // 如果未超过发送频率限制，允许请求继续处理
        return true;
    }
}
