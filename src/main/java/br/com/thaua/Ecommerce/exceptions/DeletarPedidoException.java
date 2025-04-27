package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class DeletarPedidoException extends AbstractException {
    public DeletarPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
