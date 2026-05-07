package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String type = "PERCENTAGE"; // PERCENTAGE or FLAT
    private Double value;
    private Double minOrderAmount = 0.00;
    private Double maxDiscount = 0.00;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer usageLimit = 1;
    private Integer usageCount = 0;
    private Boolean active = true;
}
