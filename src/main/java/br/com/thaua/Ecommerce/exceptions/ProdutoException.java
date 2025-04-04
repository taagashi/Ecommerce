package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ProdutoException extends AbstractException{
    public ProdutoException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
