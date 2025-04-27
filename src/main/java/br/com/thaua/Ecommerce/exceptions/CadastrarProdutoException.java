package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class CadastrarProdutoException extends AbstractException {
    public CadastrarProdutoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
