package com.musinsa.style.controller.dto.brand;

import lombok.Getter;


@Getter
public class BrandMinTotalPriceResponse {
    private BrandMinPriceDto minPrice;

    public BrandMinTotalPriceResponse() {
        minPrice = new BrandMinPriceDto();
    }

    public BrandMinTotalPriceResponse(String brandName) {
        minPrice = new BrandMinPriceDto();
        minPrice.setBrandName(brandName);
    }

    public void addCategory(String categoryName, long price) {
        BrandCategoryItemDto brandCategory = new BrandCategoryItemDto(categoryName, price);
        minPrice.addBrandCategory(brandCategory);
    }

}
