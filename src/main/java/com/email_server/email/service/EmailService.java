package com.email_server.email.service;

import com.email_server.email.TurnstileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String SITEVERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    @Value("${cloudflare.turnstile.secretkey}")
    private String SECRET_KEY;
    
    private final RestTemplate restTemplate;

    public EmailService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .build();
    }

    public TurnstileResponse validateToken(String token) {
        // Validate input
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Turnstile token validation failed: token is null or empty");
            return createErrorResponse("invalid-input");
        }

        if (SECRET_KEY == null || SECRET_KEY.trim().isEmpty()) {
            logger.error("Turnstile validation failed: SECRET_KEY not configured");
            return createErrorResponse("missing-secret-key");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", SECRET_KEY);
            params.add("response", token);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            
            logger.debug("Sending Turnstile verification request to Cloudflare API");
            ResponseEntity<TurnstileResponse> response = restTemplate.postForEntity(
                    SITEVERIFY_URL, 
                    request, 
                    TurnstileResponse.class
            );

            if (response.getBody() == null) {
                logger.error("Turnstile API returned null response body");
                return createErrorResponse("invalid-response");
            }

            TurnstileResponse turnstileResponse = response.getBody();
            
            if (!turnstileResponse.isSuccess()) {
                logger.warn("Turnstile verification failed. Error codes: {}", turnstileResponse.getErrorCodes());
            } else {
                logger.debug("Turnstile verification successful");
            }
            
            return turnstileResponse;

        } catch (RestClientException e) {
            logger.error("RestTemplate error during Turnstile verification: {}", e.getMessage(), e);
            return createErrorResponse("network-error");
        } catch (Exception e) {
            logger.error("Unexpected error during Turnstile verification: {}", e.getMessage(), e);
            return createErrorResponse("internal-error");
        }
    }

    private TurnstileResponse createErrorResponse(String errorCode) {
        TurnstileResponse errorResponse = new TurnstileResponse();
        errorResponse.setSuccess(false);
        errorResponse.setErrorCodes(List.of(errorCode));
        return errorResponse;
    }
}