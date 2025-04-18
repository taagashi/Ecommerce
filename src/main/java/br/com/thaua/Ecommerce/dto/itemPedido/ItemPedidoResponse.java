package br.com.thaua.Ecommerce.dto.itemPedido;

import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoResponse {
    private Long itemPedidoId;
    private Long produtoId;
    private String produto;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private StatusItemPedido statusItemPedido;
}
