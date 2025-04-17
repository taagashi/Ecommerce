package br.com.thaua.Ecommerce.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatusItemPedido {
    PENDENTE("Item ainda não foi enviado"),
    PROCESSANDO("Item está sendo processado"),
    ENVIADO("Item foi enviado"),
    ENTREGUE("Item foi entregue"),
    CANCELADO("Item foi cancelado");

    private final String message;
}
