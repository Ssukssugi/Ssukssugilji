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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService implements SocialAuthService {

    private static final LoginType LOGIN_TYPE = LoginType.GOOGLE;
    @Value("${google.client-id}")
    private String CLIENT_ID;

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

    private GoogleIdToken.Payload verifyToken(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory()
        )
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            return googleIdToken.getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Google ID Token verification failed ", e);
        }
    }

    private void validateResponse(GoogleUserInfoResponse response) {
        if (!response.getEmailVerified()) {
            throw new RuntimeException("Google account email is not verified");
        }
    }
}
