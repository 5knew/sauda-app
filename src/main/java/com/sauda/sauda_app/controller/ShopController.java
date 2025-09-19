package com.sauda.sauda_app.controller;

import com.sauda.sauda_app.entity.Shop;
import com.sauda.sauda_app.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shops")
@CrossOrigin(origins = "*")
public class ShopController {
    
    @Autowired
    private ShopRepository shopRepository;
    
    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops(@RequestParam(defaultValue = "1") Integer tenantId) {
        List<Shop> shops = shopRepository.findAllByTenantId(tenantId);
        return ResponseEntity.ok(shops);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer tenantId) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isPresent() && shop.get().getTenantId().equals(tenantId)) {
            return ResponseEntity.ok(shop.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Shop> getShopByTenantId(@PathVariable Integer tenantId) {
        Optional<Shop> shop = shopRepository.findByTenantId(tenantId);
        return shop.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
        Shop savedShop = shopRepository.save(shop);
        return ResponseEntity.ok(savedShop);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, 
                                          @RequestBody Shop shopDetails,
                                          @RequestParam(defaultValue = "1") Integer tenantId) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isPresent() && shopOptional.get().getTenantId().equals(tenantId)) {
            Shop shop = shopOptional.get();
            shop.setName(shopDetails.getName());
            shop.setOwnerId(shopDetails.getOwnerId());
            
            Shop updatedShop = shopRepository.save(shop);
            return ResponseEntity.ok(updatedShop);
        }
        return ResponseEntity.notFound().build();
    }
}


