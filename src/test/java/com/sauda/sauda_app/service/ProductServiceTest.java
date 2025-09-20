package com.sauda.sauda_app.service;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.repository.ProductRepository;
import com.sauda.sauda_app.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto;
    private Product product;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto();
        productDto.setTenantId(1);
        productDto.setName("Test Product");
        productDto.setBarcode("1234567890123");
        productDto.setSku("TEST-001");
        productDto.setDescription("Test Description");

        product = new Product();
        product.setId(1L);
        product.setTenantId(1);
        product.setName("Test Product");
        product.setBarcode("1234567890123");
        product.setSku("TEST-001");
        product.setDescription("Test Description");
        product.setIsActive(true);
        product.setIsDeleted(false);
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenValidData() {
        // Given
        when(productRepository.existsByBarcode(anyString())).thenReturn(false);
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product result = productService.createProduct(productDto);

        // Then
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("1234567890123", result.getBarcode());
        assertEquals("TEST-001", result.getSku());
        assertTrue(result.getIsActive());
        assertFalse(result.getIsDeleted());
        
        verify(productRepository).existsByBarcode("1234567890123");
        verify(productRepository).existsBySku("TEST-001");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenBarcodeExists() {
        // Given
        when(productRepository.existsByBarcode(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> productService.createProduct(productDto));
        
        assertEquals("Товар с штрих-кодом 1234567890123 уже существует", exception.getMessage());
        verify(productRepository).existsByBarcode("1234567890123");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenSkuExists() {
        // Given
        when(productRepository.existsByBarcode(anyString())).thenReturn(false);
        when(productRepository.existsBySku(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> productService.createProduct(productDto));
        
        assertEquals("Товар с артикулом TEST-001 уже существует", exception.getMessage());
        verify(productRepository).existsByBarcode("1234567890123");
        verify(productRepository).existsBySku("TEST-001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenNameIsEmpty() {
        // Given
        productDto.setName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> productService.createProduct(productDto));
        
        assertEquals("Название товара обязательно", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_ShouldReturnEmpty_WhenNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
    }

    @Test
    void activateProduct_ShouldActivateProduct_WhenExists() {
        // Given
        product.setIsActive(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product result = productService.activateProduct(1L);

        // Then
        assertTrue(result.getIsActive());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void activateProduct_ShouldThrowException_WhenNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> productService.activateProduct(1L));
        
        assertEquals("Товар с ID 1 не найден", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
}
