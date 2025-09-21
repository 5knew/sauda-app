package com.sauda.sauda_app.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {

    @GetMapping("/sales")
    public Map<String, String> getSalesReport(@RequestParam(defaultValue = "1") Long tenantId) {
        return Map.of(
            "message", "Sales report for tenant " + tenantId,
            "access", "MANAGER, ADMIN only",
            "totalRevenue", "500000",
            "status", "success"
        );
    }

    @GetMapping("/inventory")
    public Map<String, String> getInventoryReport(@RequestParam(defaultValue = "1") Long tenantId) {
        return Map.of(
            "message", "Inventory report for tenant " + tenantId,
            "access", "MANAGER, ADMIN only",
            "totalValue", "200000",
            "status", "success"
        );
    }

    @GetMapping("/financial")
    public Map<String, String> getFinancialReport(@RequestParam(defaultValue = "1") Long tenantId) {
        return Map.of(
            "message", "Financial report for tenant " + tenantId,
            "access", "ADMIN only",
            "profit", "100000",
            "status", "success"
        );
    }
}
