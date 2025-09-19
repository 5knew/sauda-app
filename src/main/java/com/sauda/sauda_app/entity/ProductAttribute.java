package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "product_attributes")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"attributeValues"})
public class ProductAttribute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "data_type", nullable = false)
    private String dataType;
    
    // Relationships
    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductAttributeValue> attributeValues;
}


