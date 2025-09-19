package com.sauda.sauda_app.service;

import com.sauda.sauda_app.dto.ProductDto;
import com.sauda.sauda_app.entity.Product;
import com.sauda.sauda_app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<ProductDto> getProductsByTenantId(Integer tenantId) {
        List<Product> products = productRepository.findByTenantIdAndIsActiveTrueAndIsDeletedFalse(tenantId);
        
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTenantId(product.getTenantId());
        dto.setName(product.getName());
        dto.setBarcode(product.getBarcode());
        dto.setSku(product.getSku());
        dto.setCategoryId(product.getCategoryId());
        dto.setUnitId(product.getUnitId());
        dto.setDescription(product.getDescription());
        dto.setIsActive(product.getIsActive());
        dto.setIsDeleted(product.getIsDeleted());
        
        // Set category name if category exists
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }
        
        // Set unit name if unit exists
        if (product.getUnit() != null) {
            dto.setUnitName(product.getUnit().getName());
        }
        
        return dto;
    }
}
