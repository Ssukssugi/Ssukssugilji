package com.ssukssugi.ssukssugilji.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auto-login")
public class AutoLoginController {

    @PostMapping
    public ResponseEntity<Boolean> autoLogin() {
        return ResponseEntity.ok(true);
    }
}
