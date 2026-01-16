package com.pos.service.impl;

import com.pos.exceptions.UserException;
import com.pos.mapper.BranchMapper;
import com.pos.models.Branch;
import com.pos.models.Store;
import com.pos.models.User;
import com.pos.payload.dto.BranchDto;
import com.pos.repository.BranchRepository;
import com.pos.repository.StoreRepository;
import com.pos.service.BranchService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;

    @Override
    public BranchDto createBranch(BranchDto branchDto) throws UserException {
        User currentUser = userService.getCurrentUser();
        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

        Branch branch = BranchMapper.toBranchEntity(branchDto, store);
        Branch savedBranch = branchRepository.save(branch);

        return BranchMapper.toBranchDto(savedBranch);
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto branchDto) throws Exception {

        Branch existingBranch = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch not found!")
        );

        existingBranch.setName(branchDto.getName());
        existingBranch.setAddress(branchDto.getAddress());
        existingBranch.setPhone(branchDto.getPhone());
        existingBranch.setEmail(branchDto.getEmail());
        existingBranch.setWorkingDays(branchDto.getWorkingDays());
        existingBranch.setClosingTime(branchDto.getClosingTime());
        existingBranch.setOpeningTime(branchDto.getOpeningTime());
        existingBranch.setUpdatedAt(LocalDateTime.now());

        Branch  savedBranch = branchRepository.save(existingBranch);

        return BranchMapper.toBranchDto(savedBranch);
    }

    @Override
    public void deleteBranch(Long id) throws Exception {
        Branch existingBranch = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch not found!")
        );
        branchRepository.delete(existingBranch);
    }

    @Override
    public List<BranchDto> getAllBranchesByStoreId(Long storeId) {
        List<Branch> branches =  branchRepository.findByStoreId(storeId);

        return branches.stream().map(BranchMapper::toBranchDto).toList();
    }

    @Override
    public BranchDto getBranchById(Long id) throws Exception {
        Branch existingBranch = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch not found!")
        );
        return BranchMapper.toBranchDto(existingBranch);
    }
}
