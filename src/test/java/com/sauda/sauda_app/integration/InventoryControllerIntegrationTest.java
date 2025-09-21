package com.sauda.sauda_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sauda.sauda_app.entity.Category;
import com.sauda.sauda_app.entity.Inventory;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.entity.Unit;
import com.sauda.sauda_app.entity.Warehouse;
import com.sauda.sauda_app.repository.CategoryRepository;
import com.sauda.sauda_app.repository.InventoryRepository;
import com.sauda.sauda_app.repository.ProductRepository;
import com.sauda.sauda_app.repository.UnitRepository;
import com.sauda.sauda_app.repository.WarehouseRepository;
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
class InventoryControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Product testProduct;
    private Warehouse testWarehouse;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Создаем тестовые данные
        Category category = new Category();
        category.setName("Test Category");
        category.setTenantId(1L);
        category.setCreatedAt(LocalDateTime.now());
        category.setLastUpdated(LocalDateTime.now());
        category = categoryRepository.save(category);

        Unit unit = new Unit();
        unit.setName("pcs");
        unit.setTenantId(1L);
        unit.setCreatedAt(LocalDateTime.now());
        unit.setLastUpdated(LocalDateTime.now());
        unit = unitRepository.save(unit);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setBarcode("1234567890123");
        testProduct.setSku("TEST001");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setQuantity(new BigDecimal("10"));
        testProduct.setCategory(category);
        testProduct.setUnit(unit);
        testProduct.setTenantId(1L);
        testProduct.setIsActive(true);
        testProduct.setIsDeleted(false);
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setLastUpdated(LocalDateTime.now());
        testProduct = productRepository.save(testProduct);

        testWarehouse = new Warehouse();
        testWarehouse.setName("Test Warehouse");
        testWarehouse.setAddress("123 Warehouse St");
        testWarehouse.setTenantId(1L);
        testWarehouse.setCreatedAt(LocalDateTime.now());
        testWarehouse.setLastUpdated(LocalDateTime.now());
        testWarehouse = warehouseRepository.save(testWarehouse);

        testInventory = new Inventory();
        testInventory.setProduct(testProduct);
        testInventory.setProductId(testProduct.getId());
        testInventory.setWarehouse(testWarehouse);
        testInventory.setWarehouseId(testWarehouse.getId());
        testInventory.setQuantity(new BigDecimal("50"));
        testInventory.setTenantId(1L);
        testInventory.setCreatedAt(LocalDateTime.now());
        testInventory.setLastUpdated(LocalDateTime.now());
        testInventory = inventoryRepository.save(testInventory);
    }

    @Test
    void getAllInventory_ShouldReturnInventory_WhenValidRequest() throws Exception {
        mockMvc.perform(get("/api/inventory")
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quantity").value(50))
                .andExpect(jsonPath("$.content[0].product.name").value("Test Product"))
                .andExpect(jsonPath("$.content[0].warehouse.name").value("Test Warehouse"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getInventoryById_ShouldReturnInventory_WhenValidId() throws Exception {
        mockMvc.perform(get("/api/inventory/{id}", testInventory.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.product.name").value("Test Product"))
                .andExpect(jsonPath("$.warehouse.name").value("Test Warehouse"));
    }

    @Test
    void getInventoryById_ShouldReturnNotFound_WhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/inventory/{id}", 999L)
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInventoryByProduct_ShouldReturnInventory_WhenValidProductId() throws Exception {
        mockMvc.perform(get("/api/inventory/product/{productId}", testProduct.getId())
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quantity").value(50))
                .andExpect(jsonPath("$.content[0].product.name").value("Test Product"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getInventoryByWarehouse_ShouldReturnInventory_WhenValidWarehouseId() throws Exception {
        mockMvc.perform(get("/api/inventory/warehouse/{warehouseId}", testWarehouse.getId())
                .param("tenantId", "1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quantity").value(50))
                .andExpect(jsonPath("$.content[0].warehouse.name").value("Test Warehouse"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void addStock_ShouldAddStock_WhenValidData() throws Exception {
        mockMvc.perform(post("/api/inventory/add-stock")
                .param("tenantId", "1")
                .param("productId", testProduct.getId().toString())
                .param("warehouseId", testWarehouse.getId().toString())
                .param("quantity", "25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(75)); // 50 + 25
    }

    @Test
    void removeStock_ShouldRemoveStock_WhenValidData() throws Exception {
        mockMvc.perform(post("/api/inventory/remove-stock")
                .param("tenantId", "1")
                .param("productId", testProduct.getId().toString())
                .param("warehouseId", testWarehouse.getId().toString())
                .param("quantity", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(40)); // 50 - 10
    }

    @Test
    void getLowStockAlerts_ShouldReturnLowStockItems_WhenValidThreshold() throws Exception {
        mockMvc.perform(get("/api/inventory/low-stock")
                .param("tenantId", "1")
                .param("threshold", "60")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].quantity").value(50))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTotalInventoryValue_ShouldReturnTotalValue_WhenValidWarehouse() throws Exception {
        mockMvc.perform(get("/api/inventory/total-value/warehouse/{warehouseId}", testWarehouse.getId())
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5000.00)); // 50 * 100.00
    }

    @Test
    void getTotalInventoryValue_ShouldReturnTotalValue_WhenValidTenant() throws Exception {
        mockMvc.perform(get("/api/inventory/total-value/tenant")
                .param("tenantId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5000.00)); // 50 * 100.00
    }

    @Test
    void isStockAvailable_ShouldReturnTrue_WhenStockAvailable() throws Exception {
        mockMvc.perform(get("/api/inventory/stock-available")
                .param("tenantId", "1")
                .param("productId", testProduct.getId().toString())
                .param("warehouseId", testWarehouse.getId().toString())
                .param("quantity", "30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void isStockAvailable_ShouldReturnFalse_WhenStockNotAvailable() throws Exception {
        mockMvc.perform(get("/api/inventory/stock-available")
                .param("tenantId", "1")
                .param("productId", testProduct.getId().toString())
                .param("warehouseId", testWarehouse.getId().toString())
                .param("quantity", "60")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}
