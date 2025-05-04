package br.com.thaua.Ecommerce.dto.endereco;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EnderecoResponseTest {
    @Test
    public void testEnderecoResponse() {
        Long idEndereco = 1L;
        String nameUser = "joao kleber";
        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        String estado = "estado";
        String cep = "cep";

        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(idEndereco, nameUser, rua, numero, bairro, cidade, estado, cep);

        assertThat(enderecoResponse.getIdEndereco()).isEqualTo(idEndereco);
        assertThat(enderecoResponse.getNameUser()).isEqualTo(nameUser);
        assertThat(enderecoResponse.getRua()).isEqualTo(rua);
        assertThat(enderecoResponse.getNumero()).isEqualTo(numero);
        assertThat(enderecoResponse.getBairro()).isEqualTo(bairro);
        assertThat(enderecoResponse.getCidade()).isEqualTo(cidade);
        assertThat(enderecoResponse.getEstado()).isEqualTo(estado);
        assertThat(enderecoResponse.getCep()).isEqualTo(cep);
    }
}
