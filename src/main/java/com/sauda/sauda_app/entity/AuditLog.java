package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"user"})
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "object_type", nullable = false)
    private String objectType;
    
    @Column(name = "object_id", nullable = false)
    private Long objectId;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "details_json", columnDefinition = "jsonb")
    private String detailsJson;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}


