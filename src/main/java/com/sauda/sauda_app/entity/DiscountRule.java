package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "discount_rules")
@Data
@EqualsAndHashCode(callSuper = false)
public class DiscountRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "discount_type", nullable = false)
    private String discountType;
    
    @Column(name = "discount_value", nullable = false)
    private BigDecimal value;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "applies_to")
    private String appliesTo;
}
