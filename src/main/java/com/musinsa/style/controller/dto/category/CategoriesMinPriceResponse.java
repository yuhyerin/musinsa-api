package com.musinsa.style.controller.dto.category;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class CategoriesMinPriceResponse {
    private CategoryMinPriceDto minPrice;

    public CategoriesMinPriceResponse() {
        this.minPrice = new CategoryMinPriceDto();
    }

    public CategoriesMinPriceResponse(List<CategoryItemDto> minPriceProductDtos) {
        this.minPrice = new CategoryMinPriceDto();
        minPriceProductDtos.forEach(this::addCategoryPrice);
    }

    public void addCategoryPrice(CategoryItemDto minPriceProductDto) {
        if (minPrice == null) {
            minPrice = new CategoryMinPriceDto();
        }
        minPrice.addCategoryPrice(minPriceProductDto);
    }

}


