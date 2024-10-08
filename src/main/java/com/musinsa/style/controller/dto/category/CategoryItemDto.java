package com.musinsa.style.controller.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class CategoryItemDto {
    private String categoryName;
    private String brandName;
    private long price;
}
