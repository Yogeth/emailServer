package com.email_server.email.service;

import com.email_server.email.TurnstileResponse;
import com.email_server.email.entity.EmailEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmailService {

    private static final String SITEVERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    @Value("${cloudflare.turnstile.secretkey}")
    private String SECRET_KEY;

    @Value("${spring.mail.username}")
    private String sender ;
    private final RestTemplate restTemplate = new RestTemplate();

    final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public TurnstileResponse validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", SECRET_KEY);
        params.add("response", token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<TurnstileResponse> response = restTemplate.postForEntity(SITEVERIFY_URL, request, TurnstileResponse.class);
             return response.getBody();
        } catch (Exception e) {
            TurnstileResponse errorResponse = new TurnstileResponse();
            errorResponse.setSuccess(false);
            errorResponse.setErrorCodes(List.of("internal-error"));
            return errorResponse;
        }
    }


    public boolean getDetails(EmailEntity entity )
    {

            try{
                SimpleMailMessage mailMessage = new SimpleMailMessage();

           mailMessage.setFrom(sender);
           mailMessage.setTo(sender);
           mailMessage.setSubject(entity.getSubject());
           mailMessage.setText(entity.getMessageBody());
           mailMessage.setReplyTo(entity.getReceiver());
           javaMailSender.send(mailMessage);
            return true;
            }catch(Exception e){
                e.printStackTrace();
                System.err.println("email sending failed: "+e.getMessage());
                  return false;
            }
    }
}