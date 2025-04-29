package br.com.thaua.Ecommerce.dto.pedido;

import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import lombok.Data;

@Data
public class PedidoPatchRequest {
    private StatusPedido statusPedido;
}
