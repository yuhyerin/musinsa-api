package com.musinsa.style.controller.dto.minmax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMinMaxPriceResponse {
    private String categoryName;
    private CategoryMinMaxPrice minPrice;
    private CategoryMinMaxPrice maxPrice;

    public void setMinPrice(String brandName, long price) {
        this.minPrice = createCategoryPrice(brandName, price);
    }

    public void setMaxPrice(String brandName, long price) {
        this.maxPrice = createCategoryPrice(brandName, price);
    }

    private CategoryMinMaxPrice createCategoryPrice(String brandName, long price) {
        return new CategoryMinMaxPrice(brandName, price);
    }

}
