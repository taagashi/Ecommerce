package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class ProdutoDemandaException extends AbstractException {
    public ProdutoDemandaException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
