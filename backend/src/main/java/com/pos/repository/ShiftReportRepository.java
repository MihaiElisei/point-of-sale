package com.pos.repository;

import com.pos.models.ShiftReport;
import com.pos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

    List<ShiftReport> findByCashierId(Long cashierId);
    List<ShiftReport> findByBranchId(Long branchId);

    Optional<ShiftReport> findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(User cashier);
    Optional<ShiftReport> findByCashierAndShiftStartBetween(User cashier, LocalDateTime startDate, LocalDateTime endDate);


}
