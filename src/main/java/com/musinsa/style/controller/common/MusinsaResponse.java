package com.musinsa.style.controller.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusinsaResponse<T> {
    private T data;
    private String result;
    private String message;
}
