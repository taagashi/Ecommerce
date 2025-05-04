package br.com.thaua.Ecommerce.dto.cliente;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClienteUpdateRequestTest {
    @Test
    public void testClienteUpdateRequest() {
        String name = "marcos";
        String email = "marcos@gmail.com";
        String telefone = "13323355";
        String cpf = "000.000.000-00";

        ClienteUpdateRequest clienteUpdateRequest = Fixture.createClienteUpdateRequest(name, email, telefone, cpf);

        assertThat(clienteUpdateRequest.getName()).isEqualTo(name);
        assertThat(clienteUpdateRequest.getEmail()).isEqualTo(email);
        assertThat(clienteUpdateRequest.getTelefone()).isEqualTo(telefone);
        assertThat(clienteUpdateRequest.getCpf()).isEqualTo(cpf);
    }
}
