package com.musinsa.style.controller.dto.brand;

import com.musinsa.style.repository.entity.Brand;
import lombok.Getter;

@Getter
public class BrandDtoResponse {
    private long brandId;
    private String brandName;

    public BrandDtoResponse(Brand brand) {
        this.brandId = brand.getId();
        this.brandName = brand.getName();
    }
}
