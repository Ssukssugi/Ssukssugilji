package com.ssukssugi.ssukssugilji.user.dto.setting;

import lombok.Data;

@Data
public class UserToggleSetRequest {

    private ToggleKey key;
    private Boolean value;
}
