package com.pos.service.impl;

import com.pos.mapper.InventoryMapper;
import com.pos.models.Branch;
import com.pos.models.Inventory;
import com.pos.models.Product;
import com.pos.payload.dto.InventoryDto;
import com.pos.repository.BranchRepository;
import com.pos.repository.InventoryRepository;
import com.pos.repository.ProductRepository;
import com.pos.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) throws Exception {
        Branch branch = branchRepository.findById(inventoryDto.getBranchId()).orElseThrow(
                () -> new Exception("Branch not found!")
        );

        Product product = productRepository.findById(inventoryDto.getProductId()).orElseThrow(
                () -> new Exception("Product not found!")
        );

        Inventory inventory = InventoryMapper.toInventoryEntity(inventoryDto, branch, product);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return InventoryMapper.toInventoryDto(savedInventory);
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) throws Exception {

        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory not found!")
        );

        inventory.setQuantity(inventoryDto.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);

        return InventoryMapper.toInventoryDto(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory not found!")
        );
        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDto getInventoryById(Long id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory not found!")
        );
        return InventoryMapper.toInventoryDto(inventory);
    }

    @Override
    public InventoryDto getInventoryByProductIdAndBranchId(Long productId, Long branchId) {
        Inventory inventory = inventoryRepository.findByProductIdAndBranchId(productId, branchId);
        return InventoryMapper.toInventoryDto(inventory);
    }

    @Override
    public List<InventoryDto> getAllInventoryByBranchId(Long branchId) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(branchId);
        return inventories.stream().map(InventoryMapper::toInventoryDto).toList();
    }
}
