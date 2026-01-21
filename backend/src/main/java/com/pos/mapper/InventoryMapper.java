package com.pos.mapper;

import com.pos.models.Branch;
import com.pos.models.Inventory;
import com.pos.models.Product;
import com.pos.payload.dto.InventoryDto;

public class InventoryMapper {

    public static InventoryDto toInventoryDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .product(ProductMapper.toProductDto(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .lastUpdate(inventory.getLastUpdate())
                .build();
    }

    public static Inventory toInventoryEntity(InventoryDto inventoryDto, Branch branch, Product product) {
        return Inventory.builder()
                .branch(branch)
                .product(product)
                .quantity(inventoryDto.getQuantity())
                .build();
    }
}
