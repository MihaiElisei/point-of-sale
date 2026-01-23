package com.pos.mapper;

import com.pos.models.Order;
import com.pos.models.Product;
import com.pos.models.Refund;
import com.pos.models.ShiftReport;
import com.pos.payload.dto.OrderDto;
import com.pos.payload.dto.ProductDto;
import com.pos.payload.dto.RefundDto;
import com.pos.payload.dto.ShiftReportDto;

import java.util.List;

public class ShiftReportMapper {

    public static ShiftReportDto toShiftReportDto(ShiftReport shiftReport) {
        return ShiftReportDto.builder()
                .id(shiftReport.getId())
                .shiftStart(shiftReport.getShiftStart())
                .shiftEnd(shiftReport.getShiftEnd())
                .totalSales(shiftReport.getTotalSales())
                .totalRefunds(shiftReport.getTotalRefunds())
                .netSale(shiftReport.getNetSale())
                .totalOrders(shiftReport.getTotalOrders())
                .cashier(UserMapper.toDto(shiftReport.getCashier()))
                .cashierId(shiftReport.getCashier().getId())
                .branchId(shiftReport.getBranch() != null ? shiftReport.getBranch().getId() : null)
                .recentOrders(mapOrders(shiftReport.getRecentOrders()))
                .topSellingProducts(mapProducts(shiftReport.getTopSellingProducts()))
                .refunds(mapRefunds(shiftReport.getRefunds()))
                .paymentSummaryList(shiftReport.getPaymentSummaryList())
                .build();
    }

    private static List<RefundDto> mapRefunds(List<Refund> refunds) {
        if (refunds == null) {
            return null;
        }
        return refunds.stream().map(RefundMapper::toRefundDto).toList();
    }

    private static List<ProductDto> mapProducts(List<Product> topSellingProducts) {
        if (topSellingProducts == null) {
            return null;
        }
        return topSellingProducts.stream().map(ProductMapper::toProductDto).toList();
    }

    private static List<OrderDto> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null) {
            return null;
        }
        return recentOrders.stream().map(OrderMapper::toOrderDto).toList();
    }
}
