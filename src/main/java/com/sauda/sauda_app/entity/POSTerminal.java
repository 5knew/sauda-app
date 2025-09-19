package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "pos_terminals")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"sales"})
public class POSTerminal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "is_online")
    private Boolean isOnline = true;
    
    // Relationships
    @OneToMany(mappedBy = "posTerminal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales;
}


