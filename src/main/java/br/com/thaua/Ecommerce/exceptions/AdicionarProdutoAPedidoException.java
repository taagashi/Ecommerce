package br.com.thaua.Ecommerce.exceptions;

import java.util.Map;

public class AdicionarProdutoAPedidoException extends AbstractException {
    public AdicionarProdutoAPedidoException(String message, Map<String, String> fields) {
        super(message, fields);
    }
}
