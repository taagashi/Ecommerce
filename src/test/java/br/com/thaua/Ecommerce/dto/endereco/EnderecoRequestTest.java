package br.com.thaua.Ecommerce.dto.endereco;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EnderecoRequestTest {
    @Test
    public void testEnderecoRequest() {
        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        String estado = "estado";
        String cep = "cep";
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest(rua, numero, bairro, cidade, estado, cep);

        assertThat(enderecoRequest.getRua()).isEqualTo(rua);
        assertThat(enderecoRequest.getNumero()).isEqualTo(numero);
        assertThat(enderecoRequest.getBairro()).isEqualTo(bairro);
        assertThat(enderecoRequest.getCidade()).isEqualTo(cidade);
        assertThat(enderecoRequest.getEstado()).isEqualTo(estado);
        assertThat(enderecoRequest.getCep()).isEqualTo(cep);
    }
}
