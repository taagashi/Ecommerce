package br.com.thaua.Ecommerce.dto.categoria;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProdutoComponentResponseTest {
    @Test
    public void testProdutoComponentResponse() {
        Long produtoId = 1L;
        String nome = "relogio";
        BigDecimal preco = BigDecimal.valueOf(40023.33);
        Integer estoque = 200;

        ProdutoComponentResponse produtoComponentResponse = Fixture.createProdutoComponentResponse(produtoId, nome, preco, estoque);

        assertThat(produtoComponentResponse.getProdutoId()).isEqualTo(produtoId);
        assertThat(produtoComponentResponse.getNome()).isEqualTo(nome);
        assertThat(produtoComponentResponse.getPreco()).isEqualTo(preco);
        assertThat(produtoComponentResponse.getEstoque()).isEqualTo(estoque);
    }
}
