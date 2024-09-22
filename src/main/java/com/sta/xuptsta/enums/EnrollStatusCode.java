package com.sta.xuptsta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnrollStatusCode {
    UN_ENROLLED(0),//未报名
    ENROLLED(1),//已报名
    FIRST_PASS(2),//一面通过
    SECOND_PASS(3),//二面通过
    FIRST_Fail(4),//一面未通过
    SECOND_FAIL(5); //二面未通过
    private int code;
}
