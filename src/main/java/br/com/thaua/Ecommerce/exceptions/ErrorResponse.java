package br.com.thaua.Ecommerce.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    private final Integer code;
    private final String message;
    private final List<FieldsError> fieldsErrors;
}
