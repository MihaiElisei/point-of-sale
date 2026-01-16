package com.pos.mapper;

import com.pos.models.Branch;
import com.pos.models.Store;
import com.pos.payload.dto.BranchDto;

import java.time.LocalDateTime;

public class BranchMapper {

    public static BranchDto toBranchDto(Branch branch) {
        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .closingTime(branch.getClosingTime())
                .openingTime(branch.getOpeningTime())
                .workingDays(branch.getWorkingDays())
                .storeId(branch.getStore()!=null ? branch.getStore().getId() : null)
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public static Branch toBranchEntity(BranchDto branchDto, Store store) {
        return Branch.builder()
                .name(branchDto.getName())
                .address(branchDto.getAddress())
                .phone(branchDto.getPhone())
                .store(store)
                .email(branchDto.getEmail())
                .closingTime(branchDto.getClosingTime())
                .openingTime(branchDto.getOpeningTime())
                .workingDays(branchDto.getWorkingDays())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
