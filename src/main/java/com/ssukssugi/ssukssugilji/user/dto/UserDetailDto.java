package com.ssukssugi.ssukssugilji.user.dto;

import com.ssukssugi.ssukssugilji.user.entity.PlantReason;
import com.ssukssugi.ssukssugilji.user.entity.SignUpPath;
import com.ssukssugi.ssukssugilji.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {

    private Long userId;
    private Long ageGroup;
    private PlantReason plantReason;
    private SignUpPath signUpPath;

    public static UserDetailDto fromEntity(UserDetail entity) {
        return UserDetailDto.builder()
            .userId(entity.getUser().getUserId())
            .ageGroup(entity.getAgeGroup())
            .plantReason(entity.getPlantReason())
            .signUpPath(entity.getSignUpPath())
            .build();
    }
}
