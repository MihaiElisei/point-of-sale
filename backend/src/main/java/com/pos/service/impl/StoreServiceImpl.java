package com.pos.service.impl;

import com.pos.domain.StoreStatus;
import com.pos.exceptions.UserException;
import com.pos.mapper.StoreMapper;
import com.pos.models.Store;
import com.pos.models.StoreContact;
import com.pos.models.User;
import com.pos.payload.dto.StoreDto;
import com.pos.repository.StoreRepository;
import com.pos.service.StoreService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;


    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = StoreMapper.toStoreEntity(storeDto, user);
        return StoreMapper.toStoreDto(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) throws Exception {

        Store store = storeRepository.findById(id).orElseThrow(
                () -> new Exception("Store not found!")
        );

        return StoreMapper.toStoreDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
       List<Store> stores = storeRepository.findAll();
       return stores.stream().map(StoreMapper::toStoreDto).toList();
    }

    @Override
    public Store getStoreByAdmin() throws UserException {
        User admin = userService.getCurrentUser();
        return storeRepository.findByStoreAdminId(admin.getId());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws Exception {
        User currentUser = userService.getCurrentUser();
        Store existingStore = storeRepository.findByStoreAdminId(currentUser.getId());

        if (existingStore == null) {
            throw new Exception("Store not found");
        }

        existingStore.setBrand(storeDto.getBrand());
        existingStore.setDescription(storeDto.getDescription());

        if(storeDto.getStoreType() != null){
            existingStore.setStoreType(storeDto.getStoreType());
        }

        if(storeDto.getContact() != null) {
            StoreContact contact = StoreContact.builder()
                    .address(storeDto.getContact().getAddress())
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .build();
            existingStore.setContact(contact);
        }
        Store updatedStore = storeRepository.save(existingStore);
        return StoreMapper.toStoreDto(updatedStore);
    }

    @Override
    public void deleteStore(Long id) throws UserException {
        Store store = getStoreByAdmin();
        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserException {
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new UserException("Current User doesn't exist!");
        }

        return StoreMapper.toStoreDto((currentUser.getStore()));
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus status) throws Exception {

        Store store = storeRepository.findById(id).orElseThrow(
                () -> new Exception("Store not found!")
        );

        store.setStatus(status);
        Store updatedStore = storeRepository.save(store);

        return StoreMapper.toStoreDto(updatedStore);
    }
}
