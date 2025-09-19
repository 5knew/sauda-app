package com.sauda.sauda_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "Sauda-DB Retail Management System",
            "version", "1.0.0"
        );
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
            "message", "Welcome to Sauda-DB API",
            "authentication", "Keycloak OAuth2",
            "documentation", "Available at /swagger-ui.html"
        );
    }
}
