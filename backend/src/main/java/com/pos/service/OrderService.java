package com.pos.service;

import com.pos.domain.OrderStatus;
import com.pos.domain.PaymentType;
import com.pos.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto) throws Exception;
    OrderDto getOrderById(Long orderId) throws Exception;
    List<OrderDto> getOrdersByBranch(Long branchId, Long customerId, Long cashierId, PaymentType paymentType, OrderStatus orderStatus);
    List<OrderDto> getOrderByCashierId(Long cashierId);
    List<OrderDto> getTodayOrdersByBranch(Long branchId);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getTop5RecentOrdersByBranch(Long branchId);
    void deleteOrder(Long orderId) throws Exception;
}
