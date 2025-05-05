package br.com.thaua.Ecommerce.dto.produto;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProdutoCategoriaResponseTest {
    @Test
    public void testProdutoCategoriaResponse() {
        Long categoriaId = 1L;
        String nome = "Cabelos";

        CategoriaComponentResponse categoriaComponentResponse = Fixture.createCategoriaComponentResponse(categoriaId, nome);

        Long produtoId = 1L;
        String nomeProduto = "pente";
        String preco = "12.54";
        List<CategoriaComponentResponse> categoriaResponseList = List.of(categoriaComponentResponse);

        ProdutoCategoriaResponse produtoCategoriaResponse = Fixture.createProdutoCategoriaResponse(produtoId, nomeProduto, preco,  categoriaResponseList);

        assertThat(categoriaComponentResponse.getCategoriaId()).isEqualTo(categoriaId);
        assertThat(categoriaComponentResponse.getNome()).isEqualTo(nome);

        assertThat(produtoCategoriaResponse.getProdutoId()).isEqualTo(produtoId);
        assertThat(produtoCategoriaResponse.getNome()).isEqualTo(nomeProduto);
        assertThat(produtoCategoriaResponse.getPreco()).isEqualTo(preco);
    }
}
