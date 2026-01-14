package com.pos.controller;

import com.pos.exceptions.UserException;
import com.pos.mapper.UserMapper;
import com.pos.models.User;
import com.pos.payload.dto.UserDto;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("id") Long id
    ) throws UserException, Exception {
        User user = userService.getUserById(id);

        return ResponseEntity.ok(UserMapper.toDto(user));
    }

}
