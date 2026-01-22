package com.pos.payload.dto;

import com.pos.domain.PaymentType;
import com.pos.models.Customer;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long branchId;
    private Long customerId;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private BranchDto branch;
    private UserDto cashier;
    private Customer customer;
    private List<OrderItemDto> items;
    private PaymentType paymentType;

}
