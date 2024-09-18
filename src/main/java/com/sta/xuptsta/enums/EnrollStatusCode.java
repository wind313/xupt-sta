package com.sta.xuptsta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnrollStatusCode {
    ENROLLED(0),//已报名
    FIRST_PASS(1),//一面通过
    SECOND_PASS(2),//二面通过
    Fail(3);//未通过
    private int code;
}
