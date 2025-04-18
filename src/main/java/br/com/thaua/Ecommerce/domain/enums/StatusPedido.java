package br.com.thaua.Ecommerce.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusPedido {
    CANCELADO("Pedido foi cancelado"),
    AGUARDANDO_PAGAMENTO("Aguardando pagamento do pedido"),
    PAGO("Pedido foi pago com sucesso"),
    PAGO_ENVIANDO("Itens dos pedidos estão sendo enviados"),
    ENVIADO("Pedido foi enviado para o seu endereco");

    private final String message;
}
