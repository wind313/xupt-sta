package com.sta.xuptsta.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailUtil {

    @Autowired
    private TemplateEngine templateEngine;

    public  String getCode() {
        int code = (int) (Math.random() * 10000);
        return String.format("%04d", code);
    }
    public  String getContent(String code, String name) {
        Context context = new Context();
        context.setVariable("code",code);
        return templateEngine.process(name, context);
    }
}
