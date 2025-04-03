package br.com.thaua.Ecommerce.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    private final Integer code;
    private final String message;
    private final Map<String, String> fieldsErrors;
}
