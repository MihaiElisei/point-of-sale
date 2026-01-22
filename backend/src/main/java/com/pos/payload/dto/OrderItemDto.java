package com.pos.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
    private ProductDto product;
    private Long orderId;
}
