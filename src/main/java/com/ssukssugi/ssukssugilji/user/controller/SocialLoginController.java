package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.user.application.SocialLoginApplication;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-login")
public class SocialLoginController {

    private final SocialLoginApplication socialLoginApplication;

    @PostMapping("")
    public ResponseEntity<Long> signInOrSignUp(@RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(socialLoginApplication.signInOrSignUp(request));
    }
}
