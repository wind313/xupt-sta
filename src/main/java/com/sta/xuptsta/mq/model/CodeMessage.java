package com.sta.xuptsta.mq.model;

import lombok.Data;

@Data
public class CodeMessage {
    private String email;
    private String code;
    private String content;
}
