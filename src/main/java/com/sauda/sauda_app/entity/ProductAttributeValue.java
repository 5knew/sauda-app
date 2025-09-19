package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "product_attribute_values")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"product", "attribute"})
public class ProductAttributeValue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;
    
    @Column(name = "value_string")
    private String valueString;
    
    @Column(name = "value_number")
    private BigDecimal valueNumber;
    
    @Column(name = "value_boolean")
    private Boolean valueBoolean;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", insertable = false, updatable = false)
    private ProductAttribute attribute;
}


