package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_items")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"purchaseOrder", "product"})
public class PurchaseOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "purchase_order_id", nullable = false)
    private Long purchaseOrderId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
}


