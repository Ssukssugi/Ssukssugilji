package com.ssukssugi.ssukssugilji.user.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.google.GoogleUserInfoResponse;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class GoogleAuthService implements SocialAuthService {
    
    private static final LoginType LOGIN_TYPE = LoginType.GOOGLE;
    private static final String CLIENT_ID = "341233184627-s07ukai2jbjbm6khdbnfgip5e793la72.apps.googleusercontent.com";

    @Override
    public LoginType getLoginType() {
        return LOGIN_TYPE;
    }

    @Override
    public SocialAuthUserInfoDto getAuthUserInfo(String accessToken) {
        GoogleUserInfoResponse response = getUserInfo(accessToken);
        validateResponse(response);

        return SocialAuthUserInfoDto.builder()
            .socialId(response.getLocalId())
            .emailAddress(response.getEmail())
            .loginType(LOGIN_TYPE)
            .build();
    }

    private GoogleUserInfoResponse getUserInfo(String idToken) {
        Payload payload = verifyToken(idToken);

        return GoogleUserInfoResponse.builder()
            .localId((String) payload.get("sub"))
            .email(payload.getEmail())
            .emailVerified(payload.getEmailVerified())
            .build();
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory()
        )
            .setAudience(Collections.singletonList(CLIENT_ID)) // 내 앱의 client_id 검증
            .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload(); // 유효 → 클레임 추출 가능
            } else {
                throw new RuntimeException("idToken is null");
            }
        } catch (Exception e) {
            log.error("Google ID Token verification failed: {}", e.getMessage());
            throw new RuntimeException("Invalid ID Token ", e);
        }
    }

    private void validateResponse(GoogleUserInfoResponse response) {
        if (!response.getEmailVerified()) {
            throw new RuntimeException("Google account email is not verified");
        }
    }
}
