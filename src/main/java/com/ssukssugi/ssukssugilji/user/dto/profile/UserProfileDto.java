package com.ssukssugi.ssukssugilji.user.dto.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {

    private Long userId;
    private String nickname;
}
