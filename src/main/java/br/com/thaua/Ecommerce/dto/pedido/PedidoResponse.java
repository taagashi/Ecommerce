package br.com.thaua.Ecommerce.dto.pedido;

import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Long pedidoId;
    private String cliente;
    private LocalDateTime dataPedido;
    private BigDecimal valorPedido;
    private String statusPedido;
    private List<ItemPedidoResponse> itensPedidos;
}
