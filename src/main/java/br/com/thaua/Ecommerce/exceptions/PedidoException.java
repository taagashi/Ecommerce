package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class PedidoException extends AbstractException {
    public PedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
