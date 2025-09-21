package com.sauda.sauda_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sauda.sauda_app.entity.Customer;
import com.sauda.sauda_app.repository.CustomerRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.sauda.sauda_app.config.TestIntegrationSecurityConfig;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Import(TestIntegrationSecurityConfig.class)
@Transactional
class CustomerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Создаем тестового клиента
        testCustomer = new Customer();
        testCustomer.setFullName("John Doe");
        testCustomer.setPhone("+1234567890");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setAddress("123 Main St");
        testCustomer.setDiscountCard("DC123456");
        testCustomer.setTenantId(1L);
        testCustomer.setCreatedAt(LocalDateTime.now());
        testCustomer.setLastUpdated(LocalDateTime.now());
        testCustomer.setIsDeleted(false);
        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/customers")
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].phone").value("+1234567890"))
                .andExpect(jsonPath("$.content[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenValidId() throws Exception {
        mockMvc.perform(get("/api/customers/{id}", testCustomer.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getCustomerById_ShouldReturnNotFound_WhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/customers/{id}", 999L)
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchCustomersByName_ShouldReturnMatchingCustomers_WhenValidSearchTerm() throws Exception {
        mockMvc.perform(get("/api/customers/search/name")
                .param("name", "John")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    void searchCustomersByPhone_ShouldReturnMatchingCustomers_WhenValidPhone() throws Exception {
        mockMvc.perform(get("/api/customers/search/phone")
                .param("phone", "1234567890")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].phone").value("+1234567890"));
    }

    @Test
    void getCustomersWithDiscountCards_ShouldReturnCustomers_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/customers/discount-cards")
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].discountCard").value("DC123456"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void createCustomer_ShouldCreateCustomer_WhenValidData() throws Exception {
        Customer newCustomer = new Customer();
        newCustomer.setFullName("Jane Smith");
        newCustomer.setPhone("+9876543210");
        newCustomer.setEmail("jane.smith@example.com");
        newCustomer.setAddress("456 Oak Ave");
        newCustomer.setDiscountCard("DC789012");
        newCustomer.setTenantId(1L);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Smith"))
                .andExpect(jsonPath("$.phone").value("+9876543210"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"));
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer_WhenValidData() throws Exception {
        testCustomer.setFullName("John Updated");
        testCustomer.setEmail("john.updated@example.com");
        testCustomer.setAddress("789 Pine St");

        mockMvc.perform(put("/api/customers/{id}", testCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.address").value("789 Pine St"));
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer_WhenValidId() throws Exception {
        mockMvc.perform(delete("/api/customers/{id}", testCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Проверяем, что клиент удален
        mockMvc.perform(get("/api/customers/{id}", testCustomer.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerStats_ShouldReturnStats_WhenValidTenant() throws Exception {
        mockMvc.perform(get("/api/customers/stats")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(1))
                .andExpect(jsonPath("$.customersWithDiscountCards").value(1))
                .andExpect(jsonPath("$.customersWithoutDiscountCards").value(0));
    }
}
