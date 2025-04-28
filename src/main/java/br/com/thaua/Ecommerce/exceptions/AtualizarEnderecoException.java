package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class AtualizarEnderecoException extends AbstractException {
    public AtualizarEnderecoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
