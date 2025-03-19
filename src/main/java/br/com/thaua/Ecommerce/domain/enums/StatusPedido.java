package br.com.thaua.Ecommerce.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusPedido {
    AGUARDANDO("Pedido esta em aguardo"),
    CANCELADO("Pedido foi cancelado"),
    PAGO("Pedido foi pago"),
    ENVIADO("Pedido foi enviado");

    private final String message;
}
