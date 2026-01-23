package com.pos.service.impl;

import com.pos.domain.PaymentType;
import com.pos.exceptions.UserException;
import com.pos.mapper.ShiftReportMapper;
import com.pos.models.*;
import com.pos.payload.dto.ShiftReportDto;
import com.pos.repository.OrderRepository;
import com.pos.repository.RefundRepository;
import com.pos.repository.ShiftReportRepository;
import com.pos.repository.UserRepository;
import com.pos.service.ShiftReportService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftReportServiceImpl implements ShiftReportService {

    private final ShiftReportRepository shiftReportRepository;
    private final UserService userService;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ShiftReportDto startShift() throws UserException {
        User currentUser = userService.getCurrentUser();
        LocalDateTime shiftStart = LocalDateTime.now();
        LocalDateTime startOfDay = shiftStart.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = shiftStart.withHour(23).withMinute(59).withSecond(59).withNano(0);

        Optional<ShiftReport> existingShift = shiftReportRepository.findByCashierAndShiftStartBetween(currentUser, startOfDay, endOfDay);

        if (existingShift.isPresent()) {
            throw new UserException("Shift already started");
        }

        Branch branch = currentUser.getBranch();

        ShiftReport shiftReport = ShiftReport.builder()
                .cashier(currentUser)
                .shiftStart(shiftStart)
                .branch(branch)
                .build();

        ShiftReport savedShift = shiftReportRepository.save(shiftReport);

        return ShiftReportMapper.toShiftReportDto(savedShift);
    }

    @Override
    public ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEnd) throws Exception {
        User currentUser = userService.getCurrentUser();

        ShiftReport shiftReport = shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(currentUser).orElseThrow(
                () -> new Exception("Shift not found!")
        );
        shiftReport.setShiftEnd(shiftEnd);

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(
                currentUser.getId(),shiftReport.getShiftStart(), shiftReport.getShiftEnd()
        );
        List<Order> orders = orderRepository.findByBranchIdAndCreatedAtBetween(currentUser.getId(), shiftReport.getShiftStart(), shiftReport.getShiftEnd());

        return getShiftReportDto(shiftReport, orders, refunds);
    }


    @Override
    public ShiftReportDto getShiftReportById(Long id) throws Exception {
        return ShiftReportMapper.toShiftReportDto(shiftReportRepository.findById(id).orElseThrow(
                () -> new Exception("Shift not found!")
        ));
    }

    @Override
    public List<ShiftReportDto> getAllShiftReports() {
        List<ShiftReport> shiftReports = shiftReportRepository.findAll();
        return shiftReports.stream().map(ShiftReportMapper::toShiftReportDto).collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportByCashierId(Long cashierId) {
        List<ShiftReport> shiftReports = shiftReportRepository.findByCashierId(cashierId);
        return shiftReports.stream().map(ShiftReportMapper::toShiftReportDto).collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportByBranchId(Long branchId) {
        List<ShiftReport> shiftReports = shiftReportRepository.findByBranchId(branchId);
        return shiftReports.stream().map(ShiftReportMapper::toShiftReportDto).collect(Collectors.toList());
    }

    @Override
    public ShiftReportDto getCurrentShiftProgress(Long cashierId) throws Exception {

        User user = userService.getCurrentUser();

        ShiftReport shiftReport = shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(user)
                .orElseThrow(() -> new Exception("No active shift found for this user"));
        LocalDateTime now = LocalDateTime.now();

        List<Order> orders = orderRepository.findByBranchIdAndCreatedAtBetween(
                user.getBranch().getId(), shiftReport.getShiftStart(), now
        );

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(
                user.getId(),shiftReport.getShiftStart(), now
        );

        return getShiftReportDto(shiftReport, orders, refunds);
    }

    private ShiftReportDto getShiftReportDto(ShiftReport shiftReport, List<Order> orders, List<Refund> refunds) {
        double totalRefunds = refunds.stream().mapToDouble(refund -> refund.getAmount()!=null ? refund.getAmount(): 0.0).sum();
        double totalSales = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = orders.size();
        double netSales = totalSales - totalRefunds;
        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setNetSale(netSales);
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setTopSellingProducts(getTopSellingProducts(orders));
        shiftReport.setPaymentSummaryList(getPaymentSummaries(orders, totalSales));
        shiftReport.setRefunds(refunds);

        ShiftReport savedShift = shiftReportRepository.save(shiftReport);

        return ShiftReportMapper.toShiftReportDto(savedShift);
    }

    @Override
    public ShiftReportDto getShiftByCashierAndDate(Long cashierId, LocalDateTime date) throws Exception {

        User cashier = userRepository.findById(cashierId).orElseThrow(
                () -> new Exception("Cashier not found!")
        );

        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59).withNano(0);

        ShiftReport report = shiftReportRepository.findByCashierAndShiftStartBetween(cashier, start, end)
                .orElseThrow(() -> new Exception("Shift report not found!"));


        return ShiftReportMapper.toShiftReportDto(report);
    }

    private List<PaymentSummary> getPaymentSummaries(List<Order> orders, double totalSales) {

        Map<PaymentType, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getPaymentType()!=null?order.getPaymentType():PaymentType.CASH));
        List<PaymentSummary> summaries = new ArrayList<>();

        for (Map.Entry<PaymentType, List<Order>> entry : grouped.entrySet()) {
            double amount = entry.getValue().stream()
                    .mapToDouble(Order::getTotalAmount).sum();
            int transactions = entry.getValue().size();
            double percentage = (amount / totalSales) * 100;
            PaymentSummary paymentSummary = new PaymentSummary();
            paymentSummary.setPaymentType(entry.getKey());
            paymentSummary.setTotalAmount(amount);
            paymentSummary.setTransactionCount(transactions);
            paymentSummary.setPercentage(percentage);
            summaries.add(paymentSummary);
        }
        return summaries;
    }

    private List<Product> getTopSellingProducts(List<Order> orders) {
        Map<Product, Integer> productSaleMap = new HashMap<>();
        for (Order order : orders) {
            for(OrderItem orderItem : order.getItems()) {
                Product product = orderItem.getProduct();
                productSaleMap.put(product, productSaleMap.getOrDefault(product, 0) + orderItem.getQuantity());
            }
        }
        return productSaleMap.entrySet().stream()
                .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private List<Order> getRecentOrders(List<Order> orders) {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
