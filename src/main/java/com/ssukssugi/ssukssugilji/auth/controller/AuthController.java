package com.ssukssugi.ssukssugilji.auth.controller;


import com.ssukssugi.ssukssugilji.user.application.SocialLoginApplication;
import com.ssukssugi.ssukssugilji.user.dto.SignUpRequest;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginRequest;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SocialLoginApplication socialLoginApplication;

    @PostMapping("/social-login")
    public ResponseEntity<SocialLoginResponse> signIn(
        HttpServletResponse httpServletResponse, @Valid @RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(socialLoginApplication.socialLogin(request, httpServletResponse));
    }

    // TODO: 회원가입 API 호출을 위한 임시 토큰 인증 추가
    @PostMapping("/sign-up")
    public ResponseEntity<Boolean> signUp(
        HttpServletResponse httpServletResponse, @Valid @RequestBody SignUpRequest request) {
        socialLoginApplication.signUp(request, httpServletResponse);
        return ResponseEntity.ok(true);
    }
}
