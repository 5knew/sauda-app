package com.sauda.sauda_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sauda.sauda_app.entity.Category;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.entity.Unit;
import com.sauda.sauda_app.repository.CategoryRepository;
import com.sauda.sauda_app.repository.ProductRepository;
import com.sauda.sauda_app.repository.UnitRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Import(TestIntegrationSecurityConfig.class)
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Category testCategory;
    private Unit testUnit;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Создаем тестовые данные
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setTenantId(1L);
        testCategory.setCreatedAt(LocalDateTime.now());
        testCategory.setLastUpdated(LocalDateTime.now());
        testCategory = categoryRepository.save(testCategory);

        testUnit = new Unit();
        testUnit.setName("pcs");
        testUnit.setTenantId(1L);
        testUnit.setCreatedAt(LocalDateTime.now());
        testUnit.setLastUpdated(LocalDateTime.now());
        testUnit = unitRepository.save(testUnit);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setBarcode("1234567890123");
        testProduct.setSku("TEST001");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setQuantity(new BigDecimal("10"));
        testProduct.setCategory(testCategory);
        testProduct.setUnit(testUnit);
        testProduct.setTenantId(1L);
        testProduct.setIsActive(true);
        testProduct.setIsDeleted(false);
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setLastUpdated(LocalDateTime.now());
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void getAllProducts_ShouldReturnProducts_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"))
                .andExpect(jsonPath("$.content[0].barcode").value("1234567890123"))
                .andExpect(jsonPath("$.content[0].sku").value("TEST001"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenValidId() throws Exception {
        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.barcode").value("1234567890123"))
                .andExpect(jsonPath("$.sku").value("TEST001"));
    }

    @Test
    void getProductById_ShouldReturnNotFound_WhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999L)
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts_WhenValidSearchTerm() throws Exception {
        mockMvc.perform(get("/api/products/search")
                .param("searchTerm", "Test")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductsByCategory_ShouldReturnProducts_WhenValidCategoryId() throws Exception {
        mockMvc.perform(get("/api/products/category/{categoryId}", testCategory.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenValidData() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setBarcode("9876543210987");
        newProduct.setSku("NEW001");
        newProduct.setDescription("New Description");
        newProduct.setPrice(new BigDecimal("200.00"));
        newProduct.setQuantity(new BigDecimal("5"));
        newProduct.setCategory(testCategory);
        newProduct.setUnit(testUnit);
        newProduct.setTenantId(1L);
        newProduct.setIsActive(true);
        newProduct.setIsDeleted(false);
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setLastUpdated(LocalDateTime.now());

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.barcode").value("9876543210987"))
                .andExpect(jsonPath("$.sku").value("NEW001"));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenValidData() throws Exception {
        testProduct.setName("Updated Product");
        testProduct.setDescription("Updated Description");
        testProduct.setPrice(new BigDecimal("150.00"));

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(150.00));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenValidId() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Проверяем, что продукт удален
        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
