package com.ecommerce.controller;

import com.ecommerce.entity.Coupon;
import com.ecommerce.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/{code}")
    public ResponseEntity<Coupon> validateCoupon(@PathVariable String code) {
        return couponService.getValidCoupon(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
