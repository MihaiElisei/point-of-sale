package com.pos.controller;

import com.pos.domain.OrderStatus;
import com.pos.domain.PaymentType;
import com.pos.payload.dto.OrderDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) throws Exception {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> getOrderByBranch(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false)PaymentType  paymentType,
            @RequestParam(required = false)OrderStatus status
            ) {
        return ResponseEntity.ok(orderService.getOrdersByBranch(branchId,customerId,cashierId,paymentType, status));
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<OrderDto>> getOrderByCashierId(@PathVariable Long cashierId) throws Exception {
        return ResponseEntity.ok(orderService.getOrderByCashierId(cashierId));
    }

    @GetMapping("/today/branch/{orderId}")
    public ResponseEntity<List<OrderDto>> getTodayOrder(@PathVariable Long orderId) throws Exception {
        return ResponseEntity.ok(orderService.getTodayOrdersByBranch(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getCustomerOrder(@PathVariable Long customerId) throws Exception {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }
    @GetMapping("/recent/{branchId}")
    public ResponseEntity<List<OrderDto>> getRecentOrder(@PathVariable Long branchId) throws Exception {
        return ResponseEntity.ok(orderService.getTop5RecentOrdersByBranch(branchId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable Long orderId) throws Exception {
        orderService.deleteOrder(orderId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Order has been deleted");
        return ResponseEntity.ok(apiResponse);
    }
}
