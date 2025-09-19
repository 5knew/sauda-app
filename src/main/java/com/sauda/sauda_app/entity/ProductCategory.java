package com.sauda.sauda_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"products", "children", "parent"})
public class ProductCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    // Relationships
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductCategory> children;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    private ProductCategory parent;
}


