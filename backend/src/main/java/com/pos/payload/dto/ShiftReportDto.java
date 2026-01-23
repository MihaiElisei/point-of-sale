package com.pos.payload.dto;

import com.pos.models.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ShiftReportDto {
    private Long id;
    private Long cashierId;
    private Long branchId;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    private Double totalSales;
    private Double totalRefunds;
    private Double netSale;
    private int totalOrders;
    private UserDto cashier;
    private BranchDto branch;
    private List<PaymentSummary> paymentSummaryList;
    private List<ProductDto> topSellingProducts;
    private List<OrderDto> recentOrders;
    private List<RefundDto> refunds;
}
