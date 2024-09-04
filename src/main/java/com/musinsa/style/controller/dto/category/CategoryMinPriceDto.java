package com.musinsa.style.controller.dto.category;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class CategoryMinPriceDto {
    private List<CategoryItemDto> categories;
    private long totalPrice;

    public CategoryMinPriceDto() {
        this.categories = new ArrayList<>();
    }

    public void addCategoryPrice(CategoryItemDto categoryPrice) {
        this.categories.add(categoryPrice);
        addTotalPrice(categoryPrice.getPrice());
    }

    private void addTotalPrice(long price) {
        this.totalPrice += price;
    }
}
