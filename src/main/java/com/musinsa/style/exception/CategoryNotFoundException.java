package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException(long categoryId) {
        super(ResponseCode.CATEGORY_NOT_FOUND);
        super.message = String.format(super.message, categoryId);
    }

    public CategoryNotFoundException(String categoryName) {
        super(ResponseCode.CATEGORY_NOT_FOUND);
        super.message = String.format(super.message, categoryName);
    }
}
