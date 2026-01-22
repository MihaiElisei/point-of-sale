package com.pos.mapper;

import com.pos.models.Order;
import com.pos.payload.dto.OrderDto;

public class OrderMapper {

    public static OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .branchId(order.getBranch().getId())
                .cashier(UserMapper.toDto(order.getCashier()))
                .customer(order.getCustomer())
                .paymentType(order.getPaymentType())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(
                        OrderItemMapper::toOrderItemDto
                ).toList())
                .build();
    }
}
