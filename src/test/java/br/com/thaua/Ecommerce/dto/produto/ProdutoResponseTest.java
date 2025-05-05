package br.com.thaua.Ecommerce.dto.produto;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProdutoResponseTest {
    @Test
    public void testProdutoResponse() {
        Long produtoId = 1L;
        String nome = "calça";
        String descricao = "produtode calça";
        BigDecimal preco = BigDecimal.valueOf(150);
        Integer estoque = 3;
        Integer quantidadeDemanda = 10;
        Integer categoriasAssociadas = 2;

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoId, nome, descricao, preco, estoque, quantidadeDemanda, categoriasAssociadas);

        assertThat(produtoResponse.getProdutoId()).isEqualTo(produtoId);
        assertThat(produtoResponse.getNome()).isEqualTo(nome);
        assertThat(produtoResponse.getDescricao()).isEqualTo(descricao);
        assertThat(produtoResponse.getPreco()).isEqualTo(preco);
        assertThat(produtoResponse.getEstoque()).isEqualTo(estoque);
        assertThat(produtoResponse.getQuantidadeDemanda()).isEqualTo(quantidadeDemanda);
        assertThat(produtoResponse.getCategoriasAssociadas()).isEqualTo(categoriasAssociadas);
    }
}
