package br.com.thaua.Ecommerce.dto.categoria;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoriaProdutosResponseTest {
    @Test
    public void testCategoriaProdutosResponse() {
        Long produtoComponentId = 1L;
        String produtoComponentNome = "relogio";
        BigDecimal produtoComponentPreco = BigDecimal.valueOf(40023.33);
        Integer produtoComponentEstoque = 200;

        ProdutoComponentResponse produtoComponentResponse = Fixture.createProdutoComponentResponse(produtoComponentId, produtoComponentNome, produtoComponentPreco, produtoComponentEstoque);

        Long categoriaProdutosId = 1L;
        String categoriaProdutosNome = "importados";
        String categoriaProdutosDescricao = "categoria para produtos importados";
        List<ProdutoComponentResponse> categoriaProdutosProdutos = List.of(produtoComponentResponse);

        CategoriaProdutosResponse categoriaProdutosResponse = Fixture.createCategoriaProdutosResponse(categoriaProdutosId, categoriaProdutosNome, categoriaProdutosDescricao, categoriaProdutosProdutos);

        assertThat(categoriaProdutosResponse.getCategoriaId()).isEqualTo(categoriaProdutosId);
        assertThat(categoriaProdutosResponse.getNome()).isEqualTo(categoriaProdutosNome);
        assertThat(categoriaProdutosResponse.getDescricao()).isEqualTo(categoriaProdutosDescricao);
        assertThat(categoriaProdutosResponse.getProdutos()).isEqualTo(categoriaProdutosProdutos);
        assertThat(produtoComponentResponse.getProdutoId()).isEqualTo(produtoComponentId);
        assertThat(produtoComponentResponse.getNome()).isEqualTo(produtoComponentNome);
        assertThat(produtoComponentResponse.getPreco()).isEqualTo(produtoComponentPreco);
        assertThat(produtoComponentResponse.getEstoque()).isEqualTo(produtoComponentEstoque);
    }
}
