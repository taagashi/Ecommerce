package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class ProdutoCategoriaException extends AbstractException {
    public ProdutoCategoriaException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
