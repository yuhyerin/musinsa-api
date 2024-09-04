package com.musinsa.style.controller.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class BrandCategoryItemDto {
    private String categoryName;
    private long price;
}
