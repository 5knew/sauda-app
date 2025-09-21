package com.sauda.sauda_app.service.impl;

import com.sauda.sauda_app.entity.Inventory;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.entity.Warehouse;
import com.sauda.sauda_app.repository.InventoryRepository;
import com.sauda.sauda_app.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления остатками товаров на складах
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getInventoryByWarehouse(Long warehouseId, Pageable pageable) {
        return inventoryRepository.findByWarehouseId(warehouseId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getInventoryByTenant(Long tenantId, Pageable pageable) {
        return inventoryRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByTenantAndProduct(Long tenantId, Long productId) {
        return inventoryRepository.findByTenantIdAndProductId(tenantId, productId);
    }

    @Override
    public Inventory updateInventory(Long productId, Long warehouseId, BigDecimal quantity) {
        validateQuantity(quantity);
        
        Optional<Inventory> existingInventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        
        if (existingInventory.isPresent()) {
            Inventory inventory = existingInventory.get();
            inventory.setQuantity(quantity);
            inventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepository.save(inventory);
        } else {
            // Создаем новую запись об остатке
            return createInventory(productId, warehouseId, quantity);
        }
    }

    @Override
    public Inventory increaseInventory(Long productId, Long warehouseId, BigDecimal quantity) {
        validateQuantity(quantity);
        
        Optional<Inventory> existingInventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        
        if (existingInventory.isPresent()) {
            Inventory inventory = existingInventory.get();
            BigDecimal newQuantity = inventory.getQuantity().add(quantity);
            inventory.setQuantity(newQuantity);
            inventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepository.save(inventory);
        } else {
            // Создаем новую запись об остатке
            return createInventory(productId, warehouseId, quantity);
        }
    }

    @Override
    public Inventory decreaseInventory(Long productId, Long warehouseId, BigDecimal quantity) {
        validateQuantity(quantity);
        
        Optional<Inventory> existingInventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        
        if (existingInventory.isPresent()) {
            Inventory inventory = existingInventory.get();
            BigDecimal newQuantity = inventory.getQuantity().subtract(quantity);
            
            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Недостаточно товара на складе. Доступно: " + 
                    inventory.getQuantity() + ", требуется: " + quantity);
            }
            
            inventory.setQuantity(newQuantity);
            inventory.setLastUpdated(LocalDateTime.now());
            return inventoryRepository.save(inventory);
        } else {
            throw new IllegalArgumentException("Товар не найден на складе");
        }
    }

    @Override
    public Inventory createInventory(Long productId, Long warehouseId, BigDecimal quantity) {
        validateQuantity(quantity);
        
        // Проверяем, не существует ли уже запись об остатке
        if (inventoryRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
            throw new IllegalArgumentException("Запись об остатке товара на складе уже существует");
        }
        
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setWarehouseId(warehouseId);
        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        
        // Устанавливаем tenant_id (можно получить из товара или склада)
        // Здесь предполагаем, что tenant_id передается отдельно или получается из контекста
        inventory.setTenantId(1L); // TODO: Получать из контекста безопасности
        
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalInventoryByProduct(Long tenantId, Long productId) {
        List<Inventory> inventories = inventoryRepository.findByTenantIdAndProductId(tenantId, productId);
        return inventories.stream()
                .map(Inventory::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getZeroInventoryProducts(Long tenantId, Pageable pageable) {
        return inventoryRepository.findByTenantIdAndQuantity(tenantId, BigDecimal.ZERO, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getLowInventoryProducts(Long tenantId, BigDecimal threshold, Pageable pageable) {
        return inventoryRepository.findByTenantIdAndQuantityLessThan(tenantId, threshold, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getHighInventoryProducts(Long tenantId, BigDecimal threshold, Pageable pageable) {
        return inventoryRepository.findByTenantIdAndQuantityGreaterThan(tenantId, threshold, pageable);
    }

    @Override
    public List<Inventory> transferInventory(Long productId, Long fromWarehouseId, Long toWarehouseId, BigDecimal quantity) {
        validateQuantity(quantity);
        
        // Проверяем достаточность остатка на складе-источнике
        if (!checkInventoryAvailability(productId, fromWarehouseId, quantity)) {
            throw new IllegalArgumentException("Недостаточно товара на складе-источнике для перемещения");
        }
        
        // Уменьшаем остаток на складе-источнике
        Inventory fromInventory = decreaseInventory(productId, fromWarehouseId, quantity);
        
        // Увеличиваем остаток на складе-назначения
        Inventory toInventory = increaseInventory(productId, toWarehouseId, quantity);
        
        List<Inventory> result = new ArrayList<>();
        result.add(fromInventory);
        result.add(toInventory);
        
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkInventoryAvailability(Long productId, Long warehouseId, BigDecimal requiredQuantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        
        if (inventory.isEmpty()) {
            return false;
        }
        
        return inventory.get().getQuantity().compareTo(requiredQuantity) >= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalInventoryValue(Long warehouseId) {
        // TODO: Реализовать расчет стоимости на основе цен товаров
        // Пока возвращаем 0, так как нужна интеграция с PriceList
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalInventoryValueByTenant(Long tenantId) {
        // TODO: Реализовать расчет стоимости на основе цен товаров
        // Пока возвращаем 0, так как нужна интеграция с PriceList
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public long getUniqueProductCount(Long warehouseId) {
        return inventoryRepository.countDistinctProductIdByWarehouseId(warehouseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUniqueProductCountByTenant(Long tenantId) {
        return inventoryRepository.countDistinctProductIdByTenantId(tenantId);
    }

    @Override
    public void deleteInventory(Long productId, Long warehouseId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        
        if (inventory.isPresent()) {
            inventoryRepository.delete(inventory.get());
        } else {
            throw new IllegalArgumentException("Запись об остатке товара на складе не найдена");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventory> getInventoryHistory(Long productId, Pageable pageable) {
        // TODO: Реализовать получение истории изменений остатков
        // Пока возвращаем текущие остатки товара
        return inventoryRepository.findByProductId(productId, pageable);
    }

    /**
     * Валидация количества товара
     * @param quantity количество товара
     */
    private void validateQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Количество товара не может быть null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Количество товара не может быть отрицательным");
        }
    }
}
