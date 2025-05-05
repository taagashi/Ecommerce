package br.com.thaua.Ecommerce.dto.produto;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CategoriaComponentResponseTest {
    @Test
    public void testCategoriaComponentResponse() {
        Long categoriaId = 2L;
        String nome = "esporte";
        CategoriaComponentResponse categoriaComponentResponse = Fixture.createCategoriaComponentResponse(categoriaId, nome);

        assertThat(categoriaComponentResponse.getCategoriaId()).isEqualTo(categoriaId);
        assertThat(categoriaComponentResponse.getNome()).isEqualTo(nome);
    }
}
