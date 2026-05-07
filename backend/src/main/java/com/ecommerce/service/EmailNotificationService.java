package com.ecommerce.service;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    public void sendEmail(String to, String subject, String body) {
        System.out.println("Email Sent to: " + to + " | Subject: " + subject);
    }
}
