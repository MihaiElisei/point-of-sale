package com.pos.controller;

import com.pos.models.User;
import com.pos.payload.dto.ProductDto;
import com.pos.payload.response.ApiResponse;
import com.pos.service.ProductService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create-product")
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody  ProductDto productDto,
            @RequestHeader("Authorization") String token
    ) throws Exception {

        User user = userService.getUserFromToken(token);

        return ResponseEntity.ok(productService.createProduct(productDto, user));
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDto>> searchByKeyword(
            @PathVariable  Long storeId,
            @RequestParam  String keyword
    ) throws Exception {
        return ResponseEntity.ok(productService.searchByKeyword(storeId, keyword));
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable  Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody  ProductDto productDto
    ) throws Exception {
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok(productService.updateProduct(id,productDto, user));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getByStoreId(
            @PathVariable  Long storeId,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        return ResponseEntity.ok(productService.getProductsByStoreId(storeId));
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(
            @PathVariable  Long id,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.getUserFromToken(token);
        productService.deleteProduct(id, user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }

}
