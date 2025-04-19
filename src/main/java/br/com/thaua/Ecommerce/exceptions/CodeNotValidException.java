package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class CodeNotValidException extends AbstractException {
    public CodeNotValidException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
