package br.com.thaua.Ecommerce.dto.produto;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProdutoNovoEstoqueRequestTest {
    @Test
    public void testProdutoNovoEstoqueRequest() {
        int novaQuantidade = 3;
        ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest = Fixture.createProdutoNovoEstoqueRequest(novaQuantidade);

        assertThat(produtoNovoEstoqueRequest.getNovaQuantidade()).isEqualTo(novaQuantidade);
    }
}
