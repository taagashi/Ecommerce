package br.com.thaua.Ecommerce.dto.pedido;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PedidoResponseTest {
    @Test
    public void testPedidoResponse() {
        Long itemPedidoId = 1L;
        Long produtoId = 2L;
        String produto = "camiseta";
        Integer quantidade = 2;
        BigDecimal valorTotal = BigDecimal.valueOf(100);
        StatusItemPedido statusItemPedido = StatusItemPedido.ENVIADO;

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoId, produtoId, produto, quantidade,valorTotal, statusItemPedido);

        Long pedidoId = 9L;
        String cliente = "davi";
        LocalDateTime dataPedido = LocalDateTime.now();
        BigDecimal valorPedido = BigDecimal.valueOf(100);
        String statusPedido = StatusPedido.PAGO.toString();
        List<ItemPedidoResponse> itensPedidos = List.of(itemPedidoResponse);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoId, cliente, dataPedido, valorPedido, statusPedido, itensPedidos);

        assertThat(itemPedidoResponse.getItemPedidoId()).isEqualTo(itemPedidoId);
        assertThat(itemPedidoResponse.getProdutoId()).isEqualTo(produtoId);
        assertThat(itemPedidoResponse.getProduto()).isEqualTo(produto);
        assertThat(itemPedidoResponse.getQuantidade()).isEqualTo(quantidade);
        assertThat(itemPedidoResponse.getValorTotal()).isEqualTo(valorTotal);
        assertThat(itemPedidoResponse.getStatusItemPedido()).isEqualTo(statusItemPedido);

        assertThat(pedidoResponse.getPedidoId()).isEqualTo(pedidoId);
        assertThat(pedidoResponse.getCliente()).isEqualTo(cliente);
        assertThat(pedidoResponse.getDataPedido()).isEqualTo(dataPedido);
        assertThat(pedidoResponse.getValorPedido()).isEqualTo(valorPedido);
        assertThat(pedidoResponse.getStatusPedido()).isEqualTo(statusPedido);
        assertThat(pedidoResponse.getItensPedidos().size()).isEqualTo(1);
    }
}
