package com.email_server.email.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntity {
    private String reciver;
    private String messgeBody;
    private String subject;
    private String attachment;
}