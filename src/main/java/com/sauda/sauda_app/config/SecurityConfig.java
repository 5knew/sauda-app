package com.sauda.sauda_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/debug/**").permitAll()
                .requestMatchers("/api/auth-test/public").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                
                // Product management - different access levels
                .requestMatchers("GET", "/api/products/**").hasAnyRole("USER", "CASHIER", "INVENTORY_MANAGER", "SALES_MANAGER", "MANAGER", "ADMIN")
                .requestMatchers("POST", "/api/products/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                .requestMatchers("PUT", "/api/products/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                .requestMatchers("DELETE", "/api/products/**").hasAnyRole("MANAGER", "ADMIN")
                
                // Shop management - admin and manager only
                .requestMatchers("GET", "/api/shops/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("POST", "/api/shops/**").hasAnyRole("ADMIN")
                .requestMatchers("PUT", "/api/shops/**").hasAnyRole("ADMIN")
                .requestMatchers("DELETE", "/api/shops/**").hasAnyRole("ADMIN")
                
                // Sales operations - cashier and above
                .requestMatchers("/api/sales/**").hasAnyRole("CASHIER", "SALES_MANAGER", "MANAGER", "ADMIN")
                
                // Inventory operations - inventory manager and above
                .requestMatchers("/api/inventory/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                
                // Customer management - sales manager and above
                .requestMatchers("/api/customers/**").hasAnyRole("SALES_MANAGER", "MANAGER", "ADMIN")
                
                // Reports - manager and admin only
                .requestMatchers("/api/reports/**").hasAnyRole("MANAGER", "ADMIN")
                
                // Auth test endpoints
                .requestMatchers("/api/auth-test/authenticated").authenticated()
                .requestMatchers("/api/auth-test/admin-only").hasRole("ADMIN")
                .requestMatchers("/api/auth-test/manager-only").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/api/auth-test/cashier-only").hasAnyRole("CASHIER", "SALES_MANAGER", "MANAGER", "ADMIN")
                
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        
        return jwtAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Extract roles from realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles != null) {
                for (String role : roles) {
                    // Skip default Keycloak roles
                    if (!role.equals("offline_access") && 
                        !role.equals("uma_authorization") && 
                        !role.equals("default-roles-sauda-realm")) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }
            }
        }
        
        return authorities;
    }
}
