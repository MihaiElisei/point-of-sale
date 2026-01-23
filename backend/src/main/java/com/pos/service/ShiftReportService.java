package com.pos.service;

import com.pos.exceptions.UserException;
import com.pos.models.ShiftReport;
import com.pos.payload.dto.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftReportService {

    ShiftReportDto startShift() throws UserException;
    ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEnd) throws Exception;
    ShiftReportDto getShiftReportById(Long id) throws Exception;
    List<ShiftReportDto> getAllShiftReports();
    List<ShiftReportDto> getShiftReportByCashierId(Long cashierId);
    List<ShiftReportDto> getShiftReportByBranchId(Long branchId);
    ShiftReportDto getCurrentShiftProgress(Long cashierId) throws Exception;
    ShiftReportDto getShiftByCashierAndDate(Long cashierId, LocalDateTime date) throws Exception;

}
