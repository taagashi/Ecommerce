package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class ProdutoNotFoundException extends AbstractException {
    public ProdutoNotFoundException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
