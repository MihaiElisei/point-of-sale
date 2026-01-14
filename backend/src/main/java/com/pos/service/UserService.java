package com.pos.service;

import com.pos.exceptions.UserException;
import com.pos.models.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User getUserFromToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException, Exception;
    List<User> getAllUsers();
}
