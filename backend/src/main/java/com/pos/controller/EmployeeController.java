package com.pos.controller;

import com.pos.domain.UserRole;
import com.pos.models.User;
import com.pos.payload.dto.UserDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/store/{storeId}")
    public ResponseEntity<UserDto> createStoreEmployee(
            @RequestBody UserDto userDto,
            @PathVariable Long storeId
    ) throws Exception{
        UserDto employee = employeeService.createStoreEmployee(userDto, storeId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/branch/{branchId}")
    public ResponseEntity<UserDto> createBranchEmployee(
            @RequestBody UserDto userDto,
            @PathVariable Long branchId
    ) throws Exception{
        UserDto employee = employeeService.createBranchEmployee(userDto, branchId);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateEmployee(
            @RequestBody UserDto userDto,
            @PathVariable Long id
    ) throws Exception{
        User employee = employeeService.updateEmployee(id, userDto);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(
            @PathVariable Long id
    ) throws Exception{
        employeeService.deleteEmployee(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Employee deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDto>> getStoreEmployee(
            @PathVariable Long storeId,
            @RequestParam(required = false)UserRole role
            ) throws Exception{
        List<UserDto> employee = employeeService.findStoreEmployees(storeId, role);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<UserDto>> getBranchEmployee(
            @PathVariable Long branchId,
            @RequestParam(required = false)UserRole role
    ) throws Exception{
        List<UserDto> employee = employeeService.findBranchEmployees(branchId, role);
        return ResponseEntity.ok(employee);
    }
}
