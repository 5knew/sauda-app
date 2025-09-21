package com.sauda.sauda_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.sauda.sauda_app.config.TestIntegrationSecurityConfig;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Import(TestIntegrationSecurityConfig.class)
class InventoryControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getInventory_ShouldReturnUnauthorized_WhenNoAuthentication() throws Exception {
        mockMvc.perform(get("/api/inventory")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // Ожидаем 401, так как аутентификация требуется
    }

    @Test
    void adjustInventory_ShouldAdjustInventory_WhenValidData() throws Exception {
        Map<String, Object> adjustmentData = Map.of(
            "productId", "123",
            "warehouseId", "456",
            "quantity", "10",
            "reason", "Stock adjustment"
        );

        mockMvc.perform(post("/api/inventory/adjust")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adjustmentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventory adjusted successfully"))
                .andExpect(jsonPath("$.adjustmentId").value("67890"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void getLowStockItems_ShouldReturnLowStockItems_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/inventory/low-stock")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Low stock items for tenant 1"))
                .andExpect(jsonPath("$.items").value("5"))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
