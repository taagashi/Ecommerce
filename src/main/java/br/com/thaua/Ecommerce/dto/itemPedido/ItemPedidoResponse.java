package br.com.thaua.Ecommerce.dto.itemPedido;

import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPedidoResponse {
    private Long itemPedidoId;
    private Long produtoId;
    private String produto;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private StatusItemPedido statusItemPedido;
}
