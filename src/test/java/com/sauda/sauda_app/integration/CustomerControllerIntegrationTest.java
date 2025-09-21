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
class CustomerControllerIntegrationTest {

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
    void getCustomers_ShouldReturnUnauthorized_WhenNoAuthentication() throws Exception {
        mockMvc.perform(get("/api/customers")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // Ожидаем 401, так как аутентификация требуется
    }

    @Test
    void createCustomer_ShouldCreateCustomer_WhenValidData() throws Exception {
        Map<String, Object> customerData = Map.of(
            "fullName", "Jane Smith",
            "phone", "+9876543210",
            "email", "jane.smith@example.com",
            "address", "456 Oak Ave",
            "discountCard", "DC789012"
        );

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer created successfully"))
                .andExpect(jsonPath("$.customerId").value("11111"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void getLoyaltyCustomers_ShouldReturnLoyaltyCustomers_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/customers/loyalty")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Loyalty customers for tenant 1"))
                .andExpect(jsonPath("$.count").value("25"))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
