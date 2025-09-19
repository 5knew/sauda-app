package com.sauda.sauda_app.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private Integer tenantId;
    private String name;
    private String barcode;
    private String sku;
    private Long categoryId;
    private Long unitId;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    
    // Category info without circular reference
    private String categoryName;
    
    // Unit info without circular reference
    private String unitName;
}
