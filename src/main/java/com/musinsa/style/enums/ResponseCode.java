package com.musinsa.style.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ResponseCode {
    SUCCESS("200", "성공", HttpStatus.OK),
    BAD_REQUEST("400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("500", "API 요청 처리 중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    // brand
    BRAND_NOT_FOUND("402", "브랜드(%s)가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    BRAND_DELETE_CONFLICT("409", "해당 브랜드의 상품이 존재하여 브랜드를 삭제할 수 없습니다.", HttpStatus.CONFLICT),
    // category
    CATEGORY_NOT_FOUND("402", "카테고리(%s)가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    // product
    PRODUCT_NOT_FOUND("402", "상품(%s)이 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    public final String result;
    public final String message;
    public final HttpStatus status;
}
