package br.com.thaua.Ecommerce.dto.itemPedido;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemPedidoRequestTest {
    @Test
    public void testItemPedidoRequest() {
        Long produtoId = 1L;
        int quantidade = 2;
        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(produtoId, quantidade);

        assertThat(itemPedidoRequest.getProdutoId()).isEqualTo(produtoId);
        assertThat(itemPedidoRequest.getQuantidade()).isEqualTo(quantidade);
    }
}
