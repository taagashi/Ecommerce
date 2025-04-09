package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class CategoriaNotFoundException extends AbstractException {
    public CategoriaNotFoundException(String message, Map<String, String> errors) {
        super(message, errors);
    }

}
