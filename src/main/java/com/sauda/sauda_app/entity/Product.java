package com.sauda.sauda_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"category", "unit", "attributeValues", "inventory", "salesItems", "priceList"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;
    
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(name = "unit_id")
    private Long unitId;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductCategory category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", insertable = false, updatable = false)
    @JsonIgnore
    private Unit unit;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductAttributeValue> attributeValues;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Inventory> inventory;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SalesItem> salesItems;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PriceList> priceList;
}


