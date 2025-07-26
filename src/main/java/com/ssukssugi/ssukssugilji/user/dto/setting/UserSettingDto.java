package com.ssukssugi.ssukssugilji.user.dto.setting;

import com.ssukssugi.ssukssugilji.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingDto {

    private Boolean agreeToReceiveMarketing;
    private Boolean receiveServiceNoti;

    public static UserSettingDto fromEntity(User user) {
        return UserSettingDto.builder()
            .agreeToReceiveMarketing(user.getAgreeToReceiveMarketing())
            .receiveServiceNoti(user.getReceiveServiceNoti())
            .build();
    }
}
