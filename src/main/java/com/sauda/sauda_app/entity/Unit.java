package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "units")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"products"})
public class Unit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "symbol", nullable = false)
    private String symbol;
    
    // Relationships
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}


