package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"supplier", "warehouse", "items"})
public class PurchaseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "warehouse_id")
    private Long warehouseId;
    
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private Supplier supplier;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrderItem> items;
}


