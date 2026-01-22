package com.pos.payload.dto;

import com.pos.domain.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RefundDto {

    private Long id;
    private Long orderId;
    private Long branchId;
    private Long shiftReportId;
    private OrderDto order;
    private String reason;
    private Double amount;
//    private ShiftReport shiftReport;
    private UserDto cashier;
    private String cashierName;
    private BranchDto branch;
    private PaymentType paymentType;
    private LocalDateTime createdAt;

}
