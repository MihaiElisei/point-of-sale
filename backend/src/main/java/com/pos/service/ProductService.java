package com.pos.service;

import com.pos.models.User;
import com.pos.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, User user) throws Exception;
    ProductDto updateProduct(Long id,ProductDto productDto, User user) throws Exception;
    List<ProductDto> getProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword(Long storeId, String keyword);
    void deleteProduct(Long id, User user) throws Exception;
}
