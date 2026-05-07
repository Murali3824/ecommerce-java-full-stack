package com.ecommerce.controller;

import com.ecommerce.entity.Inventory;
import com.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{variantId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long variantId) {
        return inventoryService.getInventoryByVariant(variantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
