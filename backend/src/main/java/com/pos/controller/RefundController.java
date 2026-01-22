package com.pos.controller;

import com.pos.payload.dto.RefundDto;
import com.pos.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundDto> createRefund(@RequestBody RefundDto refundDto) throws Exception{
        RefundDto refund = refundService.createRefund(refundDto);
        return ResponseEntity.ok(refund);
    }

    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefunds() throws Exception{
        List<RefundDto> refund = refundService.getAllRefunds();
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDto>> getRefundByCashier(@PathVariable Long cashierId) throws Exception{
        List<RefundDto> refund = refundService.getRefundByCashier(cashierId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDto>> getRefundsByBranch(@PathVariable Long branchId) throws Exception{
        List<RefundDto> refund = refundService.getRefundByBranchId(branchId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/shift/{shiftId}")
    public ResponseEntity<List<RefundDto>> getRefundsByShiftId(@PathVariable Long shiftId) throws Exception{
        List<RefundDto> refund = refundService.getRefundByShiftReportId(shiftId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/casier/{cashierId}/range")
    public ResponseEntity<List<RefundDto>> getRefundByCashierAndDateRange(
            @PathVariable Long cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
            ) throws Exception{
        List<RefundDto> refund = refundService.getRefundByCashierAndDateRange(cashierId, start, end);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/{refundId}")
    public ResponseEntity<RefundDto> getRefundById(@PathVariable Long refundId) throws Exception{
        RefundDto refund = refundService.getRefundById(refundId);
        return ResponseEntity.ok(refund);
    }
}
