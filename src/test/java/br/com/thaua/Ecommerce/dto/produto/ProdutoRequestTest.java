package br.com.thaua.Ecommerce.dto.produto;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProdutoRequestTest {
    @Test
    public void testProdutoRequest() {
        String nome = "relogio";
        String descricao = "produto de relogio";
        BigDecimal preco = BigDecimal.valueOf(500);
        Integer estoque = 2;

        ProdutoRequest produtoRequest = Fixture.createProdutoRequest(nome, descricao, preco, estoque);

        assertThat(produtoRequest.getNome()).isEqualTo(nome);
        assertThat(produtoRequest.getDescricao()).isEqualTo(descricao);
        assertThat(produtoRequest.getPreco()).isEqualTo(preco);
        assertThat(produtoRequest.getEstoque()).isEqualTo(estoque);

    }
}
