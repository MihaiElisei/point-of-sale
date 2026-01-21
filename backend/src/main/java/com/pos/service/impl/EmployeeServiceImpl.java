package com.pos.service.impl;

import com.pos.domain.UserRole;
import com.pos.mapper.UserMapper;
import com.pos.models.Branch;
import com.pos.models.Store;
import com.pos.models.User;
import com.pos.payload.dto.UserDto;
import com.pos.repository.BranchRepository;
import com.pos.repository.StoreRepository;
import com.pos.repository.UserRepository;
import com.pos.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(UserDto employee, Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new Exception("Store not found")
        );

        Branch branch = null;

        if(employee.getRole()==UserRole.ROLE_BRANCH_MANAGER){
            if(employee.getBranchId()==null){
                throw new Exception("Branch id is required to create branch manager!");
            }
            branch = branchRepository.findById(employee.getBranchId()).orElseThrow(
                    () -> new Exception("Branch not found")
            );
        }

        User user = UserMapper.toUserEntity(employee);
        user.setStore(store);
        user.setBranch(branch);
        user.setPassword(passwordEncoder.encode(employee.getPassword()));

        User savedEmployee = userRepository.save(user);

        if(employee.getRole()==UserRole.ROLE_BRANCH_MANAGER && branch!=null){
            branch.setManager(savedEmployee);
            branchRepository.save(branch);
        }

        return UserMapper.toDto(savedEmployee);
    }

    @Override
    public UserDto createBranchEmployee(UserDto employee, Long branchId) throws Exception {
        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new Exception("Branch not found")
        );

        if(
                employee.getRole()==UserRole.ROLE_BRANCH_CASHIER ||
                employee.getRole()==UserRole.ROLE_BRANCH_MANAGER
        ){
            User user = UserMapper.toUserEntity(employee);
            user.setBranch(branch);
            user.setPassword(passwordEncoder.encode(employee.getPassword()));

            return UserMapper.toDto(userRepository.save(user));
        }

        throw new Exception("Branch role not supported!");
    }

    @Override
    public User updateEmployee(Long employeeId, UserDto employeeDetails) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new Exception("Employee not found!")
        );

        Branch branch = branchRepository.findById(employeeDetails.getBranchId()).orElseThrow(
                () -> new Exception("Branch not found")
        );

        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setPassword(employeeDetails.getPassword());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setBranch(branch);

        return userRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) throws Exception {
        User employee =  userRepository.findById(employeeId).orElseThrow(
                () -> new Exception("Employee not found")
        );

        userRepository.delete(employee);
    }

    @Override
    public List<UserDto> findStoreEmployees(Long storeId, UserRole role) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new Exception("Store not found")
        );
        return userRepository.findByStore(store)
                .stream()
                .filter(
                        user -> role==null || user.getRole()==role
                )
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findBranchEmployees(Long branchId, UserRole role) {
        return userRepository.findByBranchId(branchId)
                .stream()
                .filter(
                        user -> role==null || user.getRole()==role
                )
                .map(UserMapper::toDto)
                .toList();
    }
}
