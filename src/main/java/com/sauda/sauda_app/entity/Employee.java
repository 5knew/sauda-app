package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"users", "sales"})
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "position", nullable = false)
    private String position;
    
    @Column(name = "hired_date", nullable = false)
    private LocalDate hiredDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    // Relationships
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;
}


