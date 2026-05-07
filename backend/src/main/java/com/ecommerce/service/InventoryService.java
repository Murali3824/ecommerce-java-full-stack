package com.ecommerce.service;

import com.ecommerce.entity.Inventory;
import com.ecommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Optional<Inventory> getInventoryByVariant(Long variantId) {
        return inventoryRepository.findByProductVariantId(variantId);
    }

    public Inventory updateStock(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
}
