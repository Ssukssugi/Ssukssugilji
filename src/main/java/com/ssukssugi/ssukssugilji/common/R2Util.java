package com.ssukssugi.ssukssugilji.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class R2Util {

    private static String STATIC_PUBLIC_URL;
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @PostConstruct
    public void init() {
        STATIC_PUBLIC_URL = publicUrl;
    }

    public static String toR2Url(String filePath) {
        return STATIC_PUBLIC_URL + filePath;
    }
}
