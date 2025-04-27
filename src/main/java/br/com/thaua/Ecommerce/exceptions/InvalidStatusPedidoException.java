package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class InvalidStatusPedidoException extends AbstractException {
    public InvalidStatusPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
