package com.pos.service.impl;

import com.pos.mapper.ProductMapper;
import com.pos.models.Category;
import com.pos.models.Product;
import com.pos.models.Store;
import com.pos.models.User;
import com.pos.payload.dto.ProductDto;
import com.pos.repository.CategoryRepository;
import com.pos.repository.ProductRepository;
import com.pos.repository.StoreRepository;
import com.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) throws Exception {
        Store store =storeRepository.findById(productDto.getStoreId()).orElseThrow(
                () -> new Exception("Store not found!")
        );
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                () -> new Exception("Category not found!")
        );
        Product product = ProductMapper.toProductEntity(productDto, store, category);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception("Product not found")
        );

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSku(productDto.getSku());
        product.setImageUrl(productDto.getImageUrl());
        product.setMrp(productDto.getMrp());
        product.setBrand(productDto.getBrand());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setUpdatedAt(LocalDateTime.now());
        if(productDto.getCategoryId()!=null){
            Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                    () -> new Exception("Category not found!")
            );
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toProductDto(updatedProduct);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        List<Product> products =  productRepository.findByStoreId(storeId);

        return  products.stream().map(ProductMapper::toProductDto).toList();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        List<Product> products =  productRepository.searchByKeyword(storeId, keyword);

        return  products.stream().map(ProductMapper::toProductDto).toList();
    }

    @Override
    public void deleteProduct(Long id, User user) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception("Product not found!")
        );
        productRepository.delete(product);
    }
}
