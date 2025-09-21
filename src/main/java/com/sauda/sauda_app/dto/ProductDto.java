package com.sauda.sauda_app.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private Long tenantId;
    private String name;
    private String barcode;
    private String sku;
    private Long categoryId;
    private Long unitId;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private BigDecimal quantity;
    
    // Category info without circular reference
    private String categoryName;
    
    // Unit info without circular reference
    private String unitName;
}
