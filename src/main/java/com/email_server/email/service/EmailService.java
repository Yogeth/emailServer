package com.email_server.email.service;

import com.email_server.email.entity.EmailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    private final String sender = "yogethcr@gmail.com";

    public String sendMail(EmailEntity emailEntity)
    {
//       try{
           SimpleMailMessage mailMessage = new SimpleMailMessage();

           mailMessage.setFrom(sender);
           mailMessage.setTo(emailEntity.getReciver());
           mailMessage.setText(emailEntity.getMessgeBody());
           javaMailSender.send(mailMessage);

           return "Mail Sent Sucessfully!";
//       }catch(Exception e){
//           return "Error while Sending Mail !";
//       }
    }
}