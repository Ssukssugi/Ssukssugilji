package com.ssukssugi.ssukssugilji.plant.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrowthVoListDto {

    private List<GrowthVo> growths;
}
