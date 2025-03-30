package br.com.thaua.Ecommerce.dto.itemPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequest {
    private Long produtoId;
    private Integer quantidade;
}
