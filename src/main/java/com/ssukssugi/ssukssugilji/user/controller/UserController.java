package com.ssukssugi.ssukssugilji.user.controller;

import com.ssukssugi.ssukssugilji.auth.service.SecurityUtil;
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

    @PostMapping("/details")
    public ResponseEntity<Boolean> saveUserDetails(UserDetailDto dto) {
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
        userService.withdraw(SecurityUtil.getUser(), response);
        return ResponseEntity.ok(true);
    }
}
