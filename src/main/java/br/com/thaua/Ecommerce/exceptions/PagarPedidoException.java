package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class PagarPedidoException extends AbstractException {
    public PagarPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
