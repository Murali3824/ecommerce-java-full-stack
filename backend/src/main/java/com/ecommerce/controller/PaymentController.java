package com.ecommerce.controller;

import com.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String signature) {
        return ResponseEntity.ok(paymentService.verifySignature(orderId, paymentId, signature));
    }
}
