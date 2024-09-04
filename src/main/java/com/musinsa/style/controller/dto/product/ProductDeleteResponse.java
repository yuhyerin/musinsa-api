package com.musinsa.style.controller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDeleteResponse {
    private List<Long> successProductIds;
    private List<Long> failProductIds;
}
