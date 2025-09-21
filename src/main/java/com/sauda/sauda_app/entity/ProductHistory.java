package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_history")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"product", "changedBy"})
public class ProductHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
    
    @Column(name = "old_value", columnDefinition = "varchar(4000)")
    private String oldValue;
    
    @Column(name = "new_value", columnDefinition = "varchar(4000)")
    private String newValue;
    
    @Column(name = "changed_by")
    private Long changedBy;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", insertable = false, updatable = false)
    private User changedByUser;
}


