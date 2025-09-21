package com.sauda.sauda_app.repository;

import com.sauda.sauda_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    List<User> findByTenantId(Long tenantId);
    
    List<User> findByTenantIdAndShopId(Long tenantId, Long shopId);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.username = :username")
    Optional<User> findByTenantIdAndUsername(@Param("tenantId") Long tenantId, @Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.roleId = :roleId")
    List<User> findByTenantIdAndRoleId(@Param("tenantId") Long tenantId, @Param("roleId") Long roleId);
}


