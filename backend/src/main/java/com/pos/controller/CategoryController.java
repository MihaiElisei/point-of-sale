package com.pos.controller;

import com.pos.payload.dto.CategoryDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) throws Exception{
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDto>> getCategoryByStoreId(
            @PathVariable Long storeId
    ) throws Exception{
        return ResponseEntity.ok(categoryService.getCategoriesByStoreId(storeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable Long id
    ) throws Exception{
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable Long id
    ) throws Exception{

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Successfully deleted category");

        return ResponseEntity.ok(apiResponse);
    }

}
