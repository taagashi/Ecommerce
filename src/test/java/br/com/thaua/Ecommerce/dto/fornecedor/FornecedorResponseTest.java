package br.com.thaua.Ecommerce.dto.fornecedor;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FornecedorResponseTest {
    @Test
    public void testFornecedorResponse() {
        Long id =  2L;
        String name = "micael";
        String email = "micael@gmail.com";
        String telefone = "(00) 00000-0000";
        String cnpj = "0000.0000/0000-00";
        FornecedorResponse fornecedorResponse = Fixture.createFornecedorResponse(id, name, email, telefone, cnpj);

        assertThat(fornecedorResponse.getId()).isEqualTo(id);
        assertThat(fornecedorResponse.getName()).isEqualTo(name);
        assertThat(fornecedorResponse.getEmail()).isEqualTo(email);
        assertThat(fornecedorResponse.getTelefone()).isEqualTo(telefone);
        assertThat(fornecedorResponse.getCnpj()).isEqualTo(cnpj);
    }
}
