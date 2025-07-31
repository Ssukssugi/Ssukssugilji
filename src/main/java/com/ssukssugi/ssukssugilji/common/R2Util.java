package com.ssukssugi.ssukssugilji.common;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class R2Util {

    private static String STATIC_PUBLIC_URL;
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @PostConstruct
    public void init() {
        if (publicUrl == null || publicUrl.isEmpty()) {
            throw new IllegalArgumentException("Cloudflare R2 public URL is not configured.");
        }
        STATIC_PUBLIC_URL = publicUrl;
    }

    public static String toR2Url(String filePath) {
        return STATIC_PUBLIC_URL + filePath;
    }
}
