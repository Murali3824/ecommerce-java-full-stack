package com.ecommerce.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSales", 14520.50);
        stats.put("totalOrders", 152);
        stats.put("totalUsers", 48);
        return stats;
    }
}
