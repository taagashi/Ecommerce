package br.com.thaua.Ecommerce.dto.itemPedido;

import lombok.Data;

@Data
public class ItemPedidoRequest {
    private Long produtoId;
    private Integer quantidade;
}
