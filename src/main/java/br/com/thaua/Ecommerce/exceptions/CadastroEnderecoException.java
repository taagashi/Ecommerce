package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class CadastroEnderecoException extends AbstractException {
    public CadastroEnderecoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
