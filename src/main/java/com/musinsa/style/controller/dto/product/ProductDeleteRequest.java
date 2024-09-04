package com.musinsa.style.controller.dto.product;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductDeleteRequest {

    @Size(max = 30, message = "상품 ID는 최대 30개까지 허용됩니다.")
    private List<Long> productIds;
}
