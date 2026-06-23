package com.email_server.email.controller;

import com.email_server.email.entity.EmailEntity;
import com.email_server.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailEntity emailEntity) {
        return emailService.sendMail(emailEntity);
    }
}