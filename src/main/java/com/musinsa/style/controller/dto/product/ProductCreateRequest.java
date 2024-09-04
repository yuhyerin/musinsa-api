package com.musinsa.style.controller.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "상품 이름은 필수값입니다.")
    private String productName;

    @Min(value = 0, message = "상품 가격은 0원 이상입니다.")
    private long price;

    @Min(value = 1, message = "카테고리 아이디는 0보다 커야합니다.")
    private long categoryId;

    @Min(value = 1, message = "브랜드 아이디는 0보다 커야합니다.")
    private long brandId;
}
