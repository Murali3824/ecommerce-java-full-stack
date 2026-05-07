package com.ecommerce.service;

import com.ecommerce.entity.Coupon;
import com.ecommerce.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Optional<Coupon> getValidCoupon(String code) {
        return couponRepository.findByCode(code)
                .filter(c -> c.getActive() 
                        && c.getStartDate().isBefore(LocalDateTime.now()) 
                        && c.getEndDate().isAfter(LocalDateTime.now()) 
                        && c.getUsageCount() < c.getUsageLimit());
    }

    public Double calculateDiscount(Coupon coupon, Double totalAmount) {
        if (totalAmount < coupon.getMinOrderAmount()) {
            return 0.00;
        }
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getType())) {
            Double discount = (coupon.getValue() / 100) * totalAmount;
            return (coupon.getMaxDiscount() > 0 && discount > coupon.getMaxDiscount()) ? coupon.getMaxDiscount() : discount;
        } else {
            return coupon.getValue();
        }
    }
}
