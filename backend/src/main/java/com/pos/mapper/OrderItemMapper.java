package com.pos.mapper;

import com.pos.models.OrderItem;
import com.pos.payload.dto.OrderItemDto;

public class OrderItemMapper {
    public static OrderItemDto toOrderItemDto(OrderItem orderItem) {

        if (orderItem == null) {
            return null;
        }

        return OrderItemDto
                .builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .product(ProductMapper.toProductDto(orderItem.getProduct()))
                .build();
    }
}
