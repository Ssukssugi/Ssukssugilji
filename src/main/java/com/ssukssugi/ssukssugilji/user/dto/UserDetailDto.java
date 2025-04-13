package com.ssukssugi.ssukssugilji.user.dto;

import com.ssukssugi.ssukssugilji.user.entity.PlantReason;
import com.ssukssugi.ssukssugilji.user.entity.SignUpPath;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {

    @NotNull
    private String nickname;
    private Long ageGroup;
    private PlantReason plantReason;
    private SignUpPath signUpPath;
}
