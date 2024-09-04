package com.musinsa.style.controller.dto.brand;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class BrandMinPriceDto {
    private String brandName;
    private List<BrandCategoryItemDto> categories;
    private long totalPrice;

    public BrandMinPriceDto() {
        categories = new ArrayList<>();
    }

    public void addBrandCategory(BrandCategoryItemDto category) {
        this.categories.add(category);
        this.totalPrice += category.getPrice();
    }
}
