package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class EnviarProdutoException extends AbstractException {
    public EnviarProdutoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
