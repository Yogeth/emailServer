package com.email_server.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntity {
    private String subject;
    private String receiver;
    private String messageBody;
    private String captchaToken;


    public EmailEntity(String subject, String receiver, String messageBody) {
        this.subject =subject;
        this.receiver=receiver;
        this.messageBody = messageBody;
    }

}