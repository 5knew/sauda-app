package com.sauda.sauda_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getInventory(@RequestParam(defaultValue = "1") Long tenantId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of(
                "error", "Unauthorized",
                "message", "Authentication required"
            ));
        }
        return ResponseEntity.ok(Map.of(
            "message", "Inventory data for tenant " + tenantId,
            "access", "INVENTORY_MANAGER, MANAGER, ADMIN only",
            "status", "success"
        ));
    }

    @PostMapping("/adjust")
    public Map<String, String> adjustInventory(@RequestBody Map<String, Object> adjustmentData) {
        return Map.of(
            "message", "Inventory adjusted successfully",
            "adjustmentId", "67890",
            "status", "success"
        );
    }

    @GetMapping("/low-stock")
    public Map<String, String> getLowStockItems(@RequestParam(defaultValue = "1") Long tenantId) {
        return Map.of(
            "message", "Low stock items for tenant " + tenantId,
            "items", "5",
            "status", "success"
        );
    }
}
