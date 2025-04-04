package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ProdutoException extends RuntimeException{
    private final Map<String, String> errors;

    public ProdutoException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
