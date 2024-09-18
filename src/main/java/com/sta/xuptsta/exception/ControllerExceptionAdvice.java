package com.sta.xuptsta.exception;

import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.result.ResultCode;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {
 
    @ExceptionHandler({BindException.class})
    public Result MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return Result.error(ResultCode.FAIL.getCode(), objectError.getDefaultMessage());
    }
}