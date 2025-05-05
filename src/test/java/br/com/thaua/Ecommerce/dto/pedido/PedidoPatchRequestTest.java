package br.com.thaua.Ecommerce.dto.pedido;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PedidoPatchRequestTest {
    @Test
    public void testPedidoPatchRequest() {
        StatusPedido statusPedido = StatusPedido.PAGO;;
        PedidoPatchRequest pedidoPatchRequest = Fixture.createPedidoPatchRequest(statusPedido);

        assertThat(pedidoPatchRequest.getStatusPedido()).isEqualTo(statusPedido);
    }
}
