package com.sta.xuptsta.mq;

import com.sta.xuptsta.exception.GlobalException;
import com.sta.xuptsta.mq.model.CodeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProvider {

    private static final String EMAIL_EXCHANGE = "xupt-sta";
    private static final String EMAIL_ROUTING_KEY = "xupt-sta-email";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmail(String email,String code,String content){
        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setEmail(email);
        codeMessage.setCode(code);
        codeMessage.setContent(content);
        try{
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE,EMAIL_ROUTING_KEY,codeMessage);
        }
        catch (Exception e){
            throw new GlobalException("发送邮件失败");
        }
    }
}
