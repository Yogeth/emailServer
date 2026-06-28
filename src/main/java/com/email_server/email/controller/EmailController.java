package com.email_server.email.controller;

import com.email_server.email.TurnstileResponse;
import com.email_server.email.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "https://my-portfolio-1kcr.vercel.app")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/captcha-verification")
    public ResponseEntity<String> getDetail(@RequestBody String token) {
        TurnstileResponse validation = emailService.validateToken(token);
        if (validation.isSuccess()) {
            System.out.println("validation sucess");
                    return ResponseEntity.ok("SUCCESS");
        } else {
            System.out.println("valitation unsucess in controller");
            return ResponseEntity.badRequest()
                    .body("Verification failed: " + validation.getErrorCodes());
        }
    }

}