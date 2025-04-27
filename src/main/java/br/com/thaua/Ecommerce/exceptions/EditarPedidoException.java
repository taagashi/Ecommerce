package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class EditarPedidoException extends AbstractException {
    public EditarPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
