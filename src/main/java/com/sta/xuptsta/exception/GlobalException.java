package com.sta.xuptsta.exception;

import com.sta.xuptsta.result.ResultCode;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException{
    private int code;
    private String message;

    public GlobalException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public GlobalException(String message) {
        this.code = ResultCode.FAIL.getCode();
        this.message = message;
    }


}
