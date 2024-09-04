package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;

public class BrandDeleteConflictException extends BaseException {
    public BrandDeleteConflictException() {
        super(ResponseCode.BRAND_DELETE_CONFLICT);
    }
}
