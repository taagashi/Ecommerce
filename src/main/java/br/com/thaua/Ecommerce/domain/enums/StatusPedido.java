package br.com.thaua.Ecommerce.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusPedido {
    CANCELADO("Pedido foi cancelado"),
    AGUARDANDO_PAGAMENTO("Aguardando pagamento do pedido"),
    PAGO("Pedido foi pago com sucesso"),
    PAGO_PROCESSANDO("Pedido está sendo processado"),
    ENVIADO("Pedido foi enviado para o seu endereco");

    private final String message;
}
