package com.ecommerce.controller;

import com.ecommerce.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// mani edited
// hi mi name is mani
// jai telangana
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getStats() {

        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }
}
