package com.sta.xuptsta.result;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    public static Result ok() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result ok(Object data){
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public static Result error(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
