package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "user_shop_roles")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"user", "shop", "role"})
public class UserShopRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "shop_id", nullable = false)
    private Long shopId;
    
    @Column(name = "role_id")
    private Long roleId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
}


