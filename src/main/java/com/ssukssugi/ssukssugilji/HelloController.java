package com.ssukssugi.ssukssugilji;

import com.ssukssugi.ssukssugilji.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, this is ssukssugilji server!";
    }
}
