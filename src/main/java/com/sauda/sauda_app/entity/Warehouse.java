package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "warehouses")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"inventory", "purchaseOrders"})
public class Warehouse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "location")
    private String location;
    
    // Relationships
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventory> inventory;
    
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrder> purchaseOrders;
}


