package com.sauda.sauda_app.controller;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.repository.ProductRepository;
import com.sauda.sauda_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "1") Long tenantId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getProductsByTenant(tenantId, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer tenantId) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().getTenantId().equals(tenantId)) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String searchTerm, 
                                                       @RequestParam(defaultValue = "1") Integer tenantId) {
        List<Product> products = productRepository.searchProducts(tenantId, searchTerm);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId, 
                                                              @RequestParam(defaultValue = "1") Integer tenantId) {
        List<Product> products = productRepository.findByTenantIdAndCategoryIdAndIsActiveTrueAndIsDeletedFalse(tenantId, categoryId);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, 
                                                @RequestParam(defaultValue = "1") Integer tenantId) {
        product.setTenantId(tenantId);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                                @RequestBody Product productDetails,
                                                @RequestParam(defaultValue = "1") Integer tenantId) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent() && productOptional.get().getTenantId().equals(tenantId)) {
            Product product = productOptional.get();
            product.setName(productDetails.getName());
            product.setBarcode(productDetails.getBarcode());
            product.setSku(productDetails.getSku());
            product.setCategoryId(productDetails.getCategoryId());
            product.setUnitId(productDetails.getUnitId());
            product.setDescription(productDetails.getDescription());
            product.setIsActive(productDetails.getIsActive());
            
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer tenantId) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent() && productOptional.get().getTenantId().equals(tenantId)) {
            Product product = productOptional.get();
            product.setIsDeleted(true);
            productRepository.save(product);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}


