package com.pos.controller;

import com.pos.payload.dto.BranchDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branch")
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/create-branch")
    public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branchDto) throws Exception {
        BranchDto createdBranch = branchService.createBranch(branchDto);
       return ResponseEntity.ok(createdBranch);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable Long id) throws Exception {
        BranchDto branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<BranchDto>> getAllBranchesByStoreId(@PathVariable Long storeId) throws Exception {
        List<BranchDto> branches = branchService.getAllBranchesByStoreId(storeId);
        return ResponseEntity.ok(branches);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable Long id, @RequestBody BranchDto branchDto) throws Exception {
        BranchDto updateBranch = branchService.updateBranch(id,branchDto);
        return ResponseEntity.ok(updateBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBranch(@PathVariable Long id) throws Exception {

        branchService.deleteBranch(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Successfully deleted branch");
        return ResponseEntity.ok(apiResponse);
    }
}
