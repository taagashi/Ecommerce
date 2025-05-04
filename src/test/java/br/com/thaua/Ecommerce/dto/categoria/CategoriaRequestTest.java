package br.com.thaua.Ecommerce.dto.categoria;

import br.com.thaua.Ecommerce.controllers.ControllersFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoriaRequestTest {
    @Test
    public void testCategoriaRequest() {
        String nome = "carro";
        String descricao = "categoria para carro";
        CategoriaRequest categoriaRequest = ControllersFixture.createCategoriaRequest(nome, descricao);

        assertThat(categoriaRequest.getNome()).isEqualTo(nome);
        assertThat(categoriaRequest.getDescricao()).isEqualTo(descricao);
    }
}
