package com.pos.repository;

import com.pos.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByCashierIdAndCreatedAtBetween(Long cashier, LocalDateTime start, LocalDateTime end);
    List<Refund> findByCashierId(Long cashierId);
    List<Refund> findByShiftReportId(Long shiftReportId);
    List<Refund> findByBranchId(Long branchId);

}
