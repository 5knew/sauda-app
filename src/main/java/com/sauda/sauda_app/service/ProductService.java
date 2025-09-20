package com.sauda.sauda_app.service;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Сервис для управления товарами
 * Предоставляет бизнес-логику для работы с товарами в системе
 */
public interface ProductService {

    /**
     * Создать новый товар
     * @param productDto DTO товара
     * @return созданный товар
     */
    Product createProduct(ProductDto productDto);

    /**
     * Обновить существующий товар
     * @param id идентификатор товара
     * @param productDto DTO товара с обновленными данными
     * @return обновленный товар
     */
    Product updateProduct(Long id, ProductDto productDto);

    /**
     * Получить товар по идентификатору
     * @param id идентификатор товара
     * @return товар или пустой Optional
     */
    Optional<Product> getProductById(Long id);

    /**
     * Получить товар по штрих-коду
     * @param barcode штрих-код товара
     * @return товар или пустой Optional
     */
    Optional<Product> getProductByBarcode(String barcode);

    /**
     * Получить товар по артикулу (SKU)
     * @param sku артикул товара
     * @return товар или пустой Optional
     */
    Optional<Product> getProductBySku(String sku);

    /**
     * Получить все товары с пагинацией
     * @param pageable параметры пагинации
     * @return страница товаров
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * Получить все товары по магазину (tenant_id)
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница товаров магазина
     */
    Page<Product> getProductsByTenant(Long tenantId, Pageable pageable);

    /**
     * Получить активные товары
     * @param pageable параметры пагинации
     * @return страница активных товаров
     */
    Page<Product> getActiveProducts(Pageable pageable);

    /**
     * Получить активные товары по магазину
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница активных товаров магазина
     */
    Page<Product> getActiveProductsByTenant(Long tenantId, Pageable pageable);

    /**
     * Поиск товаров по названию
     * @param name название товара (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных товаров
     */
    Page<Product> searchProductsByName(String name, Pageable pageable);

    /**
     * Поиск товаров по названию в рамках магазина
     * @param tenantId идентификатор магазина
     * @param name название товара (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных товаров
     */
    Page<Product> searchProductsByNameInTenant(Long tenantId, String name, Pageable pageable);

    /**
     * Получить товары по категории
     * @param categoryId идентификатор категории
     * @param pageable параметры пагинации
     * @return страница товаров категории
     */
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    /**
     * Получить товары по категории в рамках магазина
     * @param tenantId идентификатор магазина
     * @param categoryId идентификатор категории
     * @param pageable параметры пагинации
     * @return страница товаров категории
     */
    Page<Product> getProductsByCategoryInTenant(Long tenantId, Long categoryId, Pageable pageable);

    /**
     * Активировать товар
     * @param id идентификатор товара
     * @return обновленный товар
     */
    Product activateProduct(Long id);

    /**
     * Деактивировать товар
     * @param id идентификатор товара
     * @return обновленный товар
     */
    Product deactivateProduct(Long id);

    /**
     * Мягкое удаление товара (is_deleted = true)
     * @param id идентификатор товара
     */
    void softDeleteProduct(Long id);

    /**
     * Восстановить удаленный товар
     * @param id идентификатор товара
     * @return восстановленный товар
     */
    Product restoreProduct(Long id);

    /**
     * Получить количество товаров по магазину
     * @param tenantId идентификатор магазина
     * @return количество товаров
     */
    long getProductCountByTenant(Long tenantId);

    /**
     * Получить количество активных товаров по магазину
     * @param tenantId идентификатор магазина
     * @return количество активных товаров
     */
    long getActiveProductCountByTenant(Long tenantId);

    /**
     * Проверить существование товара по штрих-коду
     * @param barcode штрих-код товара
     * @return true если товар существует
     */
    boolean existsByBarcode(String barcode);

    /**
     * Проверить существование товара по артикулу
     * @param sku артикул товара
     * @return true если товар существует
     */
    boolean existsBySku(String sku);

    /**
     * Проверить существование товара по штрих-коду в рамках магазина
     * @param tenantId идентификатор магазина
     * @param barcode штрих-код товара
     * @return true если товар существует
     */
    boolean existsByBarcodeInTenant(Long tenantId, String barcode);

    /**
     * Проверить существование товара по артикулу в рамках магазина
     * @param tenantId идентификатор магазина
     * @param sku артикул товара
     * @return true если товар существует
     */
    boolean existsBySkuInTenant(Long tenantId, String sku);
}