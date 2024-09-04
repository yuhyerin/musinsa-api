package com.musinsa.style.controller.dto.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrandCreateRequest {

    @NotBlank(message = "브랜드 이름은 필수 값 입니다.")
    private String brandName;
}
