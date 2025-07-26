package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileUpdateRequest;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    public ResponseEntity<UserProfileDto> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    public ResponseEntity<Boolean> updateUserProfile(UserProfileUpdateRequest request) {
        userService.updateUserProfile(request);
        return ResponseEntity.ok(true);
    }
}
