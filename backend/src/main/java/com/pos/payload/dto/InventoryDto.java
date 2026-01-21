package com.pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InventoryDto {
    private Long id;
    private Long branchId;
    private Long productId;
    private Long customerId;
    private BranchDto branch;
    private ProductDto product;
    private Integer quantity;
    private LocalDateTime lastUpdate;
}
