package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.auth.service.JwtService;
import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.user.dto.DupNicknameCheckRequest;
import com.ssukssugi.ssukssugilji.user.dto.DupNicknameCheckResponse;
import com.ssukssugi.ssukssugilji.user.dto.UserDetailDto;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/details")
    public ResponseEntity<Boolean> saveUserDetails(@Valid @RequestBody UserDetailDto dto) {
        userService.saveUserDetail(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/dup-nickname")
    public ResponseEntity<DupNicknameCheckResponse> checkNicknameDuplicated(
        @Valid @RequestBody DupNicknameCheckRequest request) {
        DupNicknameCheckResponse response = new DupNicknameCheckResponse();
        response.setAvailable(!userService.checkNicknameExist(request.getNickname()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> withdraw(HttpServletResponse response) {
        userService.withdraw(UserContext.getUser());
        jwtService.logout(UserContext.getUser().getUserId(), response);
        return ResponseEntity.ok(true);
    }
}
