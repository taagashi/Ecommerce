package br.com.thaua.Ecommerce.dto.cliente;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClienteResponseTest {
    @Test
    public void testClienteResponse() {
        Long id = 55L;
        String name = "thiago";
        String email = "thiago@gmail.com";
        String telefone = "1233323";
        String cpf = "000.000.000-00";
        ClienteResponse clienteResponse = Fixture.createClienteResponse(id, name, email, telefone, cpf);

        assertThat(clienteResponse.getId()).isEqualTo(id);
        assertThat(clienteResponse.getName()).isEqualTo(name);
        assertThat(clienteResponse.getEmail()).isEqualTo(email);
        assertThat(clienteResponse.getTelefone()).isEqualTo(telefone);
        assertThat(clienteResponse.getCpf()).isEqualTo(cpf);
    }
}
