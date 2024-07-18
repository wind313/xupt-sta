package com.sta.xuptsta.result;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "成功"),
    FAIL(400, "系统繁忙"),
    INVALID_TOKEN(401, "token已失效"),
    NOT_LOGIN(403, "未登录");

    private int code;
    private String message;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
