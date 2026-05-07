package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPoints() {
        Map<String, Object> points = new HashMap<>();
        points.put("currentBalance", 120);
        return ResponseEntity.ok(points);
    }
}
