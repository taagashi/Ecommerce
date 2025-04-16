package br.com.thaua.Ecommerce.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusPedido {
    CANCELADO("Pedido foi cancelado"),
    PAGO("Pedido foi pago com sucesso"),
    ENVIADO("Pedido foi enviado para o seu endereco"),
    AGUARDANDO_PAGAMENTO("Aguardando pagamento do pedido");

    private final String message;
}
