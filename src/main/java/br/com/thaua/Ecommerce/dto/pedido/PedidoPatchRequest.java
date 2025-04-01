package br.com.thaua.Ecommerce.dto.pedido;

import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoPatchRequest {
    private StatusPedido statusPedido;
}
