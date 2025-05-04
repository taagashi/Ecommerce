package br.com.thaua.Ecommerce.dto.categoria;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoriaResponseTest {
    @Test
    public void testCategoriaResponse() {
        Long id = 2L;
        String nome = "panos";
        String descricao = "categoria para panos";
        int produtosAssociados = 3;
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(id, nome, descricao, produtosAssociados);

        assertThat(categoriaResponse.getId()).isEqualTo(id);
        assertThat(categoriaResponse.getNome()).isEqualTo(nome);
        assertThat(categoriaResponse.getDescricao()).isEqualTo(descricao);
        assertThat(categoriaResponse.getProdutosAssociados()).isEqualTo(produtosAssociados);
    }
}
