package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class FieldsError {
    private Map<String, String> errors;
}
