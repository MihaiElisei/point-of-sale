package com.pos.mapper;

import com.pos.models.User;
import com.pos.payload.dto.UserDto;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserDto toDto(User savedUser) {
        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setEmail(savedUser.getEmail());
        userDto.setFullName(savedUser.getFullName());
        userDto.setPhoneNumber(savedUser.getPhoneNumber());
        userDto.setRole(savedUser.getRole());
        userDto.setCreatedAt(savedUser.getCreatedAt());
        userDto.setUpdatedAt(savedUser.getUpdatedAt());
        userDto.setLastLogin(savedUser.getLastLogin());
        userDto.setBranchId(savedUser.getBranch()!=null ? savedUser.getBranch().getId() : null);
        userDto.setStoreId(savedUser.getStore()!=null ? savedUser.getStore().getId() : null);
        return userDto;
    }

    public static User toUserEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(userDto.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(userDto.getUpdatedAt());
        user.setLastLogin(userDto.getLastLogin());
        return user;
    }
}
