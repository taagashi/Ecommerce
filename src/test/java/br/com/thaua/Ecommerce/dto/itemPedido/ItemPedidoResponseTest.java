package br.com.thaua.Ecommerce.dto.itemPedido;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemPedidoResponseTest {
    @Test
    public void testItemPedidoResponse() {
        Long itemPedidoId = 1L;
        Long produtoId = 2L;
        String produto = "mala";
        Integer quantidade = 2;
        BigDecimal valorTotal = BigDecimal.valueOf(201);
        StatusItemPedido statusItemPedido = StatusItemPedido.PENDENTE;

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoId, produtoId, produto, quantidade, valorTotal, statusItemPedido);

        assertThat(itemPedidoResponse.getItemPedidoId()).isEqualTo(itemPedidoId);
        assertThat(itemPedidoResponse.getProdutoId()).isEqualTo(produtoId);
        assertThat(itemPedidoResponse.getProduto()).isEqualTo(produto);
        assertThat(itemPedidoResponse.getQuantidade()).isEqualTo(quantidade);
        assertThat(itemPedidoResponse.getValorTotal()).isEqualTo(valorTotal);
        assertThat(itemPedidoResponse.getStatusItemPedido()).isEqualTo(statusItemPedido);
    }
}
