package com.pos.service;

import com.pos.exceptions.UserException;
import com.pos.models.Refund;
import com.pos.payload.dto.RefundDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {
    RefundDto createRefund(RefundDto refundDto) throws Exception;
    List<RefundDto> getAllRefunds();
    List<RefundDto> getRefundByCashier(Long cashierId);
    List<RefundDto> getRefundByShiftReportId(Long shiftReportId);
    List<RefundDto> getRefundByCashierAndDateRange(Long cashierId, LocalDateTime startDate, LocalDateTime endDate);
    List<RefundDto> getRefundByBranchId(Long branchId);
    RefundDto getRefundById(Long refundId) throws Exception;
    void deleteRefundById(Long refundId) throws Exception;
}
