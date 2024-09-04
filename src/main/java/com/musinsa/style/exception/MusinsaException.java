package com.musinsa.style.exception;

import com.musinsa.style.enums.ResponseCode;
import org.apache.commons.lang3.StringUtils;

public class MusinsaException extends BaseException {

    public MusinsaException(ResponseCode responseCode) {
        super(responseCode);
    }

    public MusinsaException(String message) {
        super(ResponseCode.INTERNAL_SERVER_ERROR);

        if (StringUtils.isNotEmpty(message)) {
            super.message = message;
        }

    }
}
