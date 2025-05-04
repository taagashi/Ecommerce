package br.com.thaua.Ecommerce.dto.cliente;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClienteCpfTelefoneRequestTest {
    @Test
    public void testClienteCpfTelefoneRequest() {
        String cpf = "000.000.000-00";
        String telefone = "55 24 99213-1142";

        ClienteCpfTelefoneRequest clienteCpfTelefoneRequest = Fixture.createClienteCpfTelefoneRequest(cpf, telefone);

        assertThat(clienteCpfTelefoneRequest.getCpf()).isEqualTo(cpf);
        assertThat(clienteCpfTelefoneRequest.getTelefone()).isEqualTo(telefone);
    }
}
