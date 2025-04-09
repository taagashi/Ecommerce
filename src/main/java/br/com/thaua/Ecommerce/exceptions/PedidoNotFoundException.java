package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class PedidoNotFoundException extends AbstractException {
    public PedidoNotFoundException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
