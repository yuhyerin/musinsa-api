package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    protected String result;
    protected String message;
    protected HttpStatus status;

    protected BaseException(ResponseCode responseCode) {
        super(responseCode.message);

        this.result = responseCode.result;
        this.message = responseCode.message;
        this.status = responseCode.status;
    }
}
