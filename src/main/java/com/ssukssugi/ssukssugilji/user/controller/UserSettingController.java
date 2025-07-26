package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.user.dto.setting.UserSettingDto;
import com.ssukssugi.ssukssugilji.user.dto.setting.UserToggleSetRequest;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/settings")
@RequiredArgsConstructor
public class UserSettingController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserSettingDto> getUserSettings() {
        return ResponseEntity.ok(userService.getUserSettings());
    }

    @PostMapping
    public ResponseEntity<Boolean> setUserToggle(@RequestBody UserToggleSetRequest request) {
        userService.setUserToggle(request);
        return ResponseEntity.ok(true);
    }
}
