package com.sauda.sauda_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
    
    @Column(name = "error_message", nullable = false)
    private String errorMessage;
    
    @Column(name = "error_type", nullable = false)
    private String errorType;
    
    @CreationTimestamp
    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;
}
