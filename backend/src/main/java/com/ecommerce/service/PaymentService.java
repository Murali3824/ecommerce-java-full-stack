package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.PaymentTransaction;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentTransactionRepository;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Value("${app.razorpay.keySecret:rzp_test_key_secret}")
    private String keySecret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentTransactionRepository paymentRepository;

    @Transactional
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            boolean isValid = Utils.verifyPaymentSignature(payload, signature, keySecret);
            if (isValid) {
                Order order = orderRepository.findByOrderNumber(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));
                order.setStatus(com.ecommerce.entity.Order.Status.CONFIRMED);
                orderRepository.save(order);

                PaymentTransaction transaction = new PaymentTransaction();
                transaction.setTransactionId(paymentId);
                paymentRepository.save(transaction);
            }
            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}
