package com.pos.service;

import com.pos.exceptions.UserException;
import com.pos.payload.dto.UserDto;
import com.pos.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse signUp(UserDto userDto) throws UserException;
    AuthResponse login(UserDto userDto) throws UserException;
}
