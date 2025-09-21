package com.sauda.sauda_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getCustomers(@RequestParam(defaultValue = "1") Long tenantId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of(
                "error", "Unauthorized",
                "message", "Authentication required"
            ));
        }
        return ResponseEntity.ok(Map.of(
            "message", "Customers data for tenant " + tenantId,
            "access", "SALES_MANAGER, MANAGER, ADMIN only",
            "status", "success"
        ));
    }

    @PostMapping
    public Map<String, String> createCustomer(@RequestBody Map<String, Object> customerData) {
        return Map.of(
            "message", "Customer created successfully",
            "customerId", "11111",
            "status", "success"
        );
    }

    @GetMapping("/loyalty")
    public Map<String, String> getLoyaltyCustomers(@RequestParam(defaultValue = "1") Long tenantId) {
        return Map.of(
            "message", "Loyalty customers for tenant " + tenantId,
            "count", "25",
            "status", "success"
        );
    }
}
