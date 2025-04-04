package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class AddressException extends AbstractException{
    public AddressException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
