package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"sales"})
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "discount_card", unique = true)
    private String discountCard;
    
    // Relationships
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;
}


