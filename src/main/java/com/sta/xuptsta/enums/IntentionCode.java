package com.sta.xuptsta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntentionCode {
    UNKNOWN(0),//不知道
    JAVA(1),//Java
    GO(2),//Go
    FRONTEND(3);//前端
    private int code;
}
