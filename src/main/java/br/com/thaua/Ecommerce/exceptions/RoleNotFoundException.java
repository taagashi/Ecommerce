package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class RoleNotFoundException extends AbstractException {
    public RoleNotFoundException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
