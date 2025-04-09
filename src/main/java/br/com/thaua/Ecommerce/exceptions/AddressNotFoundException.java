package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class AddressNotFoundException extends  AbstractException{
    public AddressNotFoundException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
