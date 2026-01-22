package com.pos.service.impl;

import com.pos.domain.OrderStatus;
import com.pos.domain.PaymentType;
import com.pos.mapper.OrderMapper;
import com.pos.models.*;
import com.pos.payload.dto.OrderDto;
import com.pos.repository.OrderItemRepository;
import com.pos.repository.OrderRepository;
import com.pos.repository.ProductRepository;
import com.pos.service.OrderService;
import com.pos.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws Exception {
        User cashier = userService.getCurrentUser();
        Branch branch = cashier.getBranch();

        if(branch==null){
            throw new Exception("Branch not found!");
        }


        Order order = Order.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(orderDto.getCustomer())
                .paymentType(orderDto.getPaymentType())
                .build();
        List<OrderItem> items = orderDto.getItems().stream().map(itemDto -> {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Double unitPrice = product.getSellingPrice();

            if (unitPrice == null) {
                throw new IllegalStateException(
                        "Product price is null for product id: " + product.getId()
                );
            }

            return OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .price(unitPrice * itemDto.getQuantity())
                    .order(order)
                    .build();
        }).toList();
        double total = items.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalAmount(total);
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) throws Exception {

        return orderRepository.findById(orderId)
                .map(OrderMapper::toOrderDto)
                .orElseThrow(
                () -> new Exception("Order not found!")
        );
    }

    @Override
    public List<OrderDto> getOrdersByBranch(Long branchId, Long customerId, Long cashierId, PaymentType paymentType, OrderStatus orderStatus) {
        return orderRepository.findByBranchId(branchId)
                .stream()
                .filter(order -> customerId==null ||
                        (order.getCustomer()!=null) &&
                                order.getCustomer().getId().equals(customerId)
                )
                .filter(order -> cashierId == null ||
                        (order.getCashier()!=null) &&
                                order.getCashier().getId().equals(cashierId)
                )
                .filter(order -> paymentType==null ||
                        order.getPaymentType()==paymentType
                )
                .map(OrderMapper::toOrderDto)
                .toList();

    }

    @Override
    public List<OrderDto> getOrderByCashierId(Long cashierId) {
        return orderRepository.findByCashierId(cashierId).stream().map(
                OrderMapper::toOrderDto
        ).toList();
    }

    @Override
    public List<OrderDto> getTodayOrdersByBranch(Long branchId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return orderRepository.findByBranchIdAndCreatedAtBetween(branchId,start,end).stream().map(
                OrderMapper::toOrderDto
        ).toList();
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(
                OrderMapper::toOrderDto
        ).toList();
    }

    @Override
    public List<OrderDto> getTop5RecentOrdersByBranch(Long branchId) {
        return orderRepository.findTop5ByBranchIdOrderByCreatedAtDesc(branchId).stream().map(
                OrderMapper::toOrderDto
        ).toList();
    }

    @Override
    public void deleteOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new Exception("Order not found!")
        );
        orderRepository.delete(order);
    }
}
