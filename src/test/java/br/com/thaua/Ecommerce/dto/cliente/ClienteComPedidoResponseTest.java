package br.com.thaua.Ecommerce.dto.cliente;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClienteComPedidoResponseTest {
    @Test
    public void testClienteComPedidoResponse() {
         Long id = 4L;
         String name = "anderson";
         String email = "anderson@gmail.com";
         String telefone = "55 13 99194-1008";
         String cpf = "000.000.000-00";
         int pedidosFeitos  = 4;
        ClienteComPedidoResponse clienteComPedidoResponse = Fixture.createClienteComPedidoResponse(id, name, email, telefone, cpf, pedidosFeitos);

        assertThat(clienteComPedidoResponse.getId()).isEqualTo(id);
        assertThat(clienteComPedidoResponse.getName()).isEqualTo(name);
        assertThat(clienteComPedidoResponse.getEmail()).isEqualTo(email);
        assertThat(clienteComPedidoResponse.getTelefone()).isEqualTo(telefone);
        assertThat(clienteComPedidoResponse.getCpf()).isEqualTo(cpf);
        assertThat(clienteComPedidoResponse.getPedidosFeitos()).isEqualTo(pedidosFeitos);
    }
}
