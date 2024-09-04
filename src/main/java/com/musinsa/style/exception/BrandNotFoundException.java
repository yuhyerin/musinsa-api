package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;

public class BrandNotFoundException extends BaseException {

    public BrandNotFoundException(long brandId) {
        super(ResponseCode.BRAND_NOT_FOUND);
        super.message = String.format(super.message, brandId);
    }
}
