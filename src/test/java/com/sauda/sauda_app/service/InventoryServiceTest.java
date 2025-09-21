package com.sauda.sauda_app.service;

import com.sauda.sauda_app.entity.Inventory;
import com.sauda.sauda_app.repository.InventoryRepository;
import com.sauda.sauda_app.service.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private final Long productId = 1L;
    private final Long warehouseId = 1L;
    private final BigDecimal quantity = new BigDecimal("100.00");

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductId(productId);
        inventory.setWarehouseId(warehouseId);
        inventory.setQuantity(quantity);
        inventory.setTenantId(1L);
        inventory.setLastUpdated(LocalDateTime.now());
    }

    @Test
    void getInventoryByProductAndWarehouse_ShouldReturnInventory_WhenExists() {
        // Given
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        // When
        Optional<Inventory> result = inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(quantity, result.get().getQuantity());
        assertEquals(productId, result.get().getProductId());
        assertEquals(warehouseId, result.get().getWarehouseId());
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    void getInventoryByProductAndWarehouse_ShouldReturnEmpty_WhenNotExists() {
        // Given
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());

        // When
        Optional<Inventory> result = inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);

        // Then
        assertFalse(result.isPresent());
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    void updateInventory_ShouldUpdateExistingInventory_WhenExists() {
        // Given
        BigDecimal newQuantity = new BigDecimal("150.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.updateInventory(productId, warehouseId, newQuantity);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void updateInventory_ShouldCreateNewInventory_WhenNotExists() {
        // Given
        BigDecimal newQuantity = new BigDecimal("150.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());
        when(inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(false);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.updateInventory(productId, warehouseId, newQuantity);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void increaseInventory_ShouldIncreaseQuantity_WhenInventoryExists() {
        // Given
        BigDecimal increaseAmount = new BigDecimal("50.00");
        BigDecimal expectedQuantity = quantity.add(increaseAmount);
        
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.increaseInventory(productId, warehouseId, increaseAmount);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void increaseInventory_ShouldCreateNewInventory_WhenNotExists() {
        // Given
        BigDecimal increaseAmount = new BigDecimal("50.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());
        when(inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(false);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.increaseInventory(productId, warehouseId, increaseAmount);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void decreaseInventory_ShouldDecreaseQuantity_WhenSufficientStock() {
        // Given
        BigDecimal decreaseAmount = new BigDecimal("30.00");
        BigDecimal expectedQuantity = quantity.subtract(decreaseAmount);
        
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.decreaseInventory(productId, warehouseId, decreaseAmount);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void decreaseInventory_ShouldThrowException_WhenInsufficientStock() {
        // Given
        BigDecimal decreaseAmount = new BigDecimal("150.00"); // More than available
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.decreaseInventory(productId, warehouseId, decreaseAmount));
        
        assertTrue(exception.getMessage().contains("Недостаточно товара на складе"));
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void decreaseInventory_ShouldThrowException_WhenInventoryNotExists() {
        // Given
        BigDecimal decreaseAmount = new BigDecimal("50.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.decreaseInventory(productId, warehouseId, decreaseAmount));
        
        assertTrue(exception.getMessage().contains("Товар не найден на складе"));
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void createInventory_ShouldCreateNewInventory_WhenValidData() {
        // Given
        BigDecimal newQuantity = new BigDecimal("200.00");
        when(inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(false);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        Inventory result = inventoryService.createInventory(productId, warehouseId, newQuantity);

        // Then
        assertNotNull(result);
        verify(inventoryRepository).existsByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void createInventory_ShouldThrowException_WhenAlreadyExists() {
        // Given
        BigDecimal newQuantity = new BigDecimal("200.00");
        when(inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.createInventory(productId, warehouseId, newQuantity));
        
        assertTrue(exception.getMessage().contains("Запись об остатке товара на складе уже существует"));
        verify(inventoryRepository).existsByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void createInventory_ShouldThrowException_WhenQuantityIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.createInventory(productId, warehouseId, null));
        
        assertTrue(exception.getMessage().contains("Количество товара не может быть null"));
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void createInventory_ShouldThrowException_WhenQuantityIsNegative() {
        // Given
        BigDecimal negativeQuantity = new BigDecimal("-10.00");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.createInventory(productId, warehouseId, negativeQuantity));
        
        assertTrue(exception.getMessage().contains("Количество товара не может быть отрицательным"));
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void getTotalInventoryByProduct_ShouldReturnSum_WhenMultipleInventories() {
        // Given
        Long tenantId = 1L;
        List<Inventory> inventories = new ArrayList<>();
        
        Inventory inventory1 = new Inventory();
        inventory1.setQuantity(new BigDecimal("50.00"));
        inventories.add(inventory1);
        
        Inventory inventory2 = new Inventory();
        inventory2.setQuantity(new BigDecimal("75.00"));
        inventories.add(inventory2);
        
        when(inventoryRepository.findByTenantIdAndProductId(tenantId, productId))
                .thenReturn(inventories);

        // When
        BigDecimal result = inventoryService.getTotalInventoryByProduct(tenantId, productId);

        // Then
        assertEquals(new BigDecimal("125.00"), result);
        verify(inventoryRepository).findByTenantIdAndProductId(tenantId, productId);
    }

    @Test
    void checkInventoryAvailability_ShouldReturnTrue_WhenSufficientStock() {
        // Given
        BigDecimal requiredQuantity = new BigDecimal("50.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        // When
        boolean result = inventoryService.checkInventoryAvailability(productId, warehouseId, requiredQuantity);

        // Then
        assertTrue(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    void checkInventoryAvailability_ShouldReturnFalse_WhenInsufficientStock() {
        // Given
        BigDecimal requiredQuantity = new BigDecimal("150.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        // When
        boolean result = inventoryService.checkInventoryAvailability(productId, warehouseId, requiredQuantity);

        // Then
        assertFalse(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    void checkInventoryAvailability_ShouldReturnFalse_WhenInventoryNotExists() {
        // Given
        BigDecimal requiredQuantity = new BigDecimal("50.00");
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());

        // When
        boolean result = inventoryService.checkInventoryAvailability(productId, warehouseId, requiredQuantity);

        // Then
        assertFalse(result);
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    void deleteInventory_ShouldDeleteInventory_WhenExists() {
        // Given
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        // When
        inventoryService.deleteInventory(productId, warehouseId);

        // Then
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository).delete(inventory);
    }

    @Test
    void deleteInventory_ShouldThrowException_WhenNotExists() {
        // Given
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.deleteInventory(productId, warehouseId));
        
        assertTrue(exception.getMessage().contains("Запись об остатке товара на складе не найдена"));
        verify(inventoryRepository).findByProductIdAndWarehouseId(productId, warehouseId);
        verify(inventoryRepository, never()).delete(any(Inventory.class));
    }
}
