package com.ssukssugi.ssukssugilji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SsukssugiljiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsukssugiljiApplication.class, args);
    }

}
