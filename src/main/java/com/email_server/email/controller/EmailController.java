package com.email_server.email.controller;

import com.email_server.email.TurnstileResponse;
import com.email_server.email.entity.EmailEntity;
import com.email_server.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/captcha-verification")
    public ResponseEntity<String> getDetail(@RequestBody EmailEntity emailEntity)
    {
       TurnstileResponse validation= emailService.validateToken(emailEntity.getCaptchaToken());
        if (validation.isSuccess()) {
           String email= emailService.getDetails(new EmailEntity(emailEntity.getSubject(),emailEntity.getReceiver(),emailEntity.getMessageBody()));
            System.out.println("valitation success in controller");
            System.out.println(email);
            return ResponseEntity.ok("Form submitted successfully");

        } else {
            System.out.println("valitation unsucess in controller");
            return ResponseEntity.badRequest()
                    .body("Verification failed: " + validation.getErrorCodes());
        }
    }

}