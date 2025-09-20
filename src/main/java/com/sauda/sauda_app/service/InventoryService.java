package com.sauda.sauda_app.service;

import com.sauda.sauda_app.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления остатками товаров на складах
 * Предоставляет бизнес-логику для работы с инвентарем
 */
public interface InventoryService {

    /**
     * Получить остаток товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @return остаток товара или пустой Optional
     */
    Optional<Inventory> getInventoryByProductAndWarehouse(Long productId, Long warehouseId);

    /**
     * Получить все остатки товара по всем складам
     * @param productId идентификатор товара
     * @return список остатков товара
     */
    List<Inventory> getInventoryByProduct(Long productId);

    /**
     * Получить все остатки на складе
     * @param warehouseId идентификатор склада
     * @param pageable параметры пагинации
     * @return страница остатков на складе
     */
    Page<Inventory> getInventoryByWarehouse(Long warehouseId, Pageable pageable);

    /**
     * Получить все остатки по магазину
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница остатков магазина
     */
    Page<Inventory> getInventoryByTenant(Long tenantId, Pageable pageable);

    /**
     * Получить остатки товара по магазину
     * @param tenantId идентификатор магазина
     * @param productId идентификатор товара
     * @return список остатков товара в магазине
     */
    List<Inventory> getInventoryByTenantAndProduct(Long tenantId, Long productId);

    /**
     * Обновить остаток товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @param quantity новое количество
     * @return обновленный остаток
     */
    Inventory updateInventory(Long productId, Long warehouseId, BigDecimal quantity);

    /**
     * Увеличить остаток товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @param quantity количество для увеличения
     * @return обновленный остаток
     */
    Inventory increaseInventory(Long productId, Long warehouseId, BigDecimal quantity);

    /**
     * Уменьшить остаток товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @param quantity количество для уменьшения
     * @return обновленный остаток
     */
    Inventory decreaseInventory(Long productId, Long warehouseId, BigDecimal quantity);

    /**
     * Создать запись об остатке товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @param quantity начальное количество
     * @return созданный остаток
     */
    Inventory createInventory(Long productId, Long warehouseId, BigDecimal quantity);

    /**
     * Получить общий остаток товара по всем складам магазина
     * @param tenantId идентификатор магазина
     * @param productId идентификатор товара
     * @return общий остаток товара
     */
    BigDecimal getTotalInventoryByProduct(Long tenantId, Long productId);

    /**
     * Получить товары с нулевым остатком
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница товаров с нулевым остатком
     */
    Page<Inventory> getZeroInventoryProducts(Long tenantId, Pageable pageable);

    /**
     * Получить товары с низким остатком (меньше указанного порога)
     * @param tenantId идентификатор магазина
     * @param threshold пороговое значение
     * @param pageable параметры пагинации
     * @return страница товаров с низким остатком
     */
    Page<Inventory> getLowInventoryProducts(Long tenantId, BigDecimal threshold, Pageable pageable);

    /**
     * Получить товары с высоким остатком (больше указанного порога)
     * @param tenantId идентификатор магазина
     * @param threshold пороговое значение
     * @param pageable параметры пагинации
     * @return страница товаров с высоким остатком
     */
    Page<Inventory> getHighInventoryProducts(Long tenantId, BigDecimal threshold, Pageable pageable);

    /**
     * Переместить товар между складами
     * @param productId идентификатор товара
     * @param fromWarehouseId идентификатор склада-источника
     * @param toWarehouseId идентификатор склада-назначения
     * @param quantity количество для перемещения
     * @return список обновленных остатков
     */
    List<Inventory> transferInventory(Long productId, Long fromWarehouseId, Long toWarehouseId, BigDecimal quantity);

    /**
     * Проверить достаточность остатка для продажи
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     * @param requiredQuantity требуемое количество
     * @return true если остатка достаточно
     */
    boolean checkInventoryAvailability(Long productId, Long warehouseId, BigDecimal requiredQuantity);

    /**
     * Получить общую стоимость остатков на складе
     * @param warehouseId идентификатор склада
     * @return общая стоимость остатков
     */
    BigDecimal getTotalInventoryValue(Long warehouseId);

    /**
     * Получить общую стоимость остатков по магазину
     * @param tenantId идентификатор магазина
     * @return общая стоимость остатков
     */
    BigDecimal getTotalInventoryValueByTenant(Long tenantId);

    /**
     * Получить количество уникальных товаров на складе
     * @param warehouseId идентификатор склада
     * @return количество уникальных товаров
     */
    long getUniqueProductCount(Long warehouseId);

    /**
     * Получить количество уникальных товаров по магазину
     * @param tenantId идентификатор магазина
     * @return количество уникальных товаров
     */
    long getUniqueProductCountByTenant(Long tenantId);

    /**
     * Удалить запись об остатке товара на складе
     * @param productId идентификатор товара
     * @param warehouseId идентификатор склада
     */
    void deleteInventory(Long productId, Long warehouseId);

    /**
     * Получить историю изменений остатков товара
     * @param productId идентификатор товара
     * @param pageable параметры пагинации
     * @return страница истории изменений
     */
    Page<Inventory> getInventoryHistory(Long productId, Pageable pageable);
}
