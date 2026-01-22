package com.pos.service.impl;

import com.pos.exceptions.UserException;
import com.pos.mapper.RefundMapper;
import com.pos.models.Branch;
import com.pos.models.Order;
import com.pos.models.Refund;
import com.pos.models.User;
import com.pos.payload.dto.RefundDto;
import com.pos.repository.OrderRepository;
import com.pos.repository.RefundRepository;
import com.pos.service.RefundService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;

    @Override
    public RefundDto createRefund(RefundDto refundDto) throws Exception {
        User cashier  = userService.getCurrentUser();
        Order order = orderRepository.findById(refundDto.getOrderId()).orElseThrow(
                () -> new Exception("Order not found!")
        );
        Branch branch = order.getBranch();

        Refund createdRefund = Refund.builder()
                .order(order)
                .cashier(cashier)
                .branch(branch)
                .reason(refundDto.getReason())
                .amount(refundDto.getAmount())
                .createdAt(refundDto.getCreatedAt())
                .build();

        Refund savedRefund = refundRepository.save(createdRefund);
        return RefundMapper.toRefundDto(savedRefund);
    }

    @Override
    public List<RefundDto> getAllRefunds() {
        return refundRepository.findAll().stream().map(RefundMapper::toRefundDto).toList();
    }

    @Override
    public List<RefundDto> getRefundByCashier(Long cashierId) {
        return refundRepository.findByCashierId(cashierId).stream().map(RefundMapper::toRefundDto).toList();
    }

    @Override
    public List<RefundDto> getRefundByShiftReportId(Long shiftReportId) {
        return refundRepository.findByShiftReportId(shiftReportId).stream().map(RefundMapper::toRefundDto).toList();
    }

    @Override
    public List<RefundDto> getRefundByCashierAndDateRange(Long cashierId, LocalDateTime startDate, LocalDateTime endDate) {
        return refundRepository.findByCashierIdAndCreatedAtBetween(cashierId,startDate,endDate).stream().map(RefundMapper::toRefundDto).toList();
    }

    @Override
    public List<RefundDto> getRefundByBranchId(Long branchId) {
        return refundRepository.findByBranchId(branchId).stream().map(RefundMapper::toRefundDto).toList();
    }

    @Override
    public RefundDto getRefundById(Long refundId) throws Exception {
        return refundRepository.findById(refundId).map(RefundMapper::toRefundDto).orElseThrow(
                () -> new Exception("Refund not found")
        );
    }

    @Override
    public void deleteRefundById(Long refundId) throws Exception {
        this.getRefundById(refundId);
        refundRepository.deleteById(refundId);
    }
}
