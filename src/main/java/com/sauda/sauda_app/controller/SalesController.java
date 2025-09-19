package com.sauda.sauda_app.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    @GetMapping
    public Map<String, String> getSales(@RequestParam(defaultValue = "1") Integer tenantId) {
        return Map.of(
            "message", "Sales data for tenant " + tenantId,
            "access", "CASHIER, SALES_MANAGER, MANAGER, ADMIN only",
            "status", "success"
        );
    }

    @PostMapping
    public Map<String, String> createSale(@RequestBody Map<String, Object> saleData) {
        return Map.of(
            "message", "Sale created successfully",
            "saleId", "12345",
            "status", "success"
        );
    }

    @GetMapping("/reports")
    public Map<String, String> getSalesReports(@RequestParam(defaultValue = "1") Integer tenantId) {
        return Map.of(
            "message", "Sales reports for tenant " + tenantId,
            "totalSales", "100000",
            "status", "success"
        );
    }
}
