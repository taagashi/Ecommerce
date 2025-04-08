package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class UserNotFoundException extends AbstractException {
    public UserNotFoundException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
