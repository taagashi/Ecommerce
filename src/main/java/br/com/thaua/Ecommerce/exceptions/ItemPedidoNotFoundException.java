package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class ItemPedidoNotFoundException extends AbstractException {
    public ItemPedidoNotFoundException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
