package com.sauda.sauda_app.controller;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllProducts() throws Exception {
        // Given
        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("iPhone 15 Pro");
        product1.setBarcode("1234567890123");
        product1.setTenantId(1);

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Samsung Galaxy S24");
        product2.setBarcode("9876543210987");
        product2.setTenantId(1);

        List<ProductDto> products = Arrays.asList(product1, product2);
        when(productService.getProductsByTenantId(anyInt())).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy S24"));
    }

    @Test
    void testGetAllProductsWithoutAuth() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
