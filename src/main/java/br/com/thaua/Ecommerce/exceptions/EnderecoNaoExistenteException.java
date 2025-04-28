package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class EnderecoNaoExistenteException extends AbstractException {
    public EnderecoNaoExistenteException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
