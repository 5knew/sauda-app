package com.sauda.sauda_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth-test")
public class AuthTestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a public endpoint - no authentication required");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<Map<String, Object>> authenticatedEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is an authenticated endpoint");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-only")
    public ResponseEntity<Map<String, Object>> adminOnlyEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is an admin-only endpoint");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/manager-only")
    public ResponseEntity<Map<String, Object>> managerOnlyEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a manager-only endpoint");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cashier-only")
    public ResponseEntity<Map<String, Object>> cashierOnlyEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a cashier-only endpoint");
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
