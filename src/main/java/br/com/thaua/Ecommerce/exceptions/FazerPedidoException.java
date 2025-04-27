package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class FazerPedidoException extends AbstractException {
    public FazerPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
