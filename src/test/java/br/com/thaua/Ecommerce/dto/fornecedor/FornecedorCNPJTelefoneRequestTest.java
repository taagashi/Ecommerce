package br.com.thaua.Ecommerce.dto.fornecedor;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FornecedorCNPJTelefoneRequestTest {
    @Test
    public void testFornecedorCNPJTelefoneRequest() {
        String cnpj = "0000.0000/0000-00";
        String telefone = "(00) 00000-0000";
        FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest = Fixture.createFornecedorCNPJTelefoneRequest(cnpj, telefone);

        assertThat(fornecedorCNPJTelefoneRequest.getCnpj()).isEqualTo(cnpj);
        assertThat(fornecedorCNPJTelefoneRequest.getTelefone()).isEqualTo(telefone);
    }
}
