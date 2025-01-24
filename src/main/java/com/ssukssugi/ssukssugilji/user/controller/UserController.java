package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.user.dto.UserDetailDto;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/details")
    public ResponseEntity<Boolean> saveUserDetails(UserDetailDto dto) {
        userService.saveUserDetail(dto);
        return ResponseEntity.ok(true);
    }
}
