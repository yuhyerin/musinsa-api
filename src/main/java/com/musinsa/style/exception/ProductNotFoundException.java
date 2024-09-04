package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;

import java.util.List;

public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException(long productId) {
        super(ResponseCode.PRODUCT_NOT_FOUND);
        super.message = String.format(super.message, productId);
    }

    public ProductNotFoundException(List<Long> productId) {
        super(ResponseCode.PRODUCT_NOT_FOUND);
        super.message = String.format(super.message, productId.toString());
    }
}
