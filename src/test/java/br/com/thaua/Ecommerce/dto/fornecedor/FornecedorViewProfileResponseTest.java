package br.com.thaua.Ecommerce.dto.fornecedor;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FornecedorViewProfileResponseTest {
    @Test
    public void testFornecedorViewProfileResponse() {
        Long id = 100L;
        String name = "jonatas";
        String email = "jonatas@gmail.com";
        String telefone = "(00) 00000-0000";
        String cnpj = "0000.0000/0000-00";
        BigDecimal saldo = BigDecimal.valueOf(20);
        Integer produtosEnviados = 5;
        Integer produtosCadastrados = 10;
        FornecedorViewProfileResponse fornecedorViewProfileResponse = Fixture.createFornecedorViewProfileResponse(id, name, email, telefone, cnpj, saldo, produtosEnviados, produtosCadastrados);

        assertThat(fornecedorViewProfileResponse.getId()).isEqualTo(id);
        assertThat(fornecedorViewProfileResponse.getName()).isEqualTo(name);
        assertThat(fornecedorViewProfileResponse.getEmail()).isEqualTo(email);
        assertThat(fornecedorViewProfileResponse.getTelefone()).isEqualTo(telefone);
        assertThat(fornecedorViewProfileResponse.getCnpj()).isEqualTo(cnpj);
        assertThat(fornecedorViewProfileResponse.getSaldo()).isEqualTo(saldo);
        assertThat(fornecedorViewProfileResponse.getProdutosEnviados()).isEqualTo(produtosEnviados);
        assertThat(fornecedorViewProfileResponse.getProdutosCadastrados()).isEqualTo(produtosCadastrados);
    }
}
