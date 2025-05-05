package br.com.thaua.Ecommerce.dto.fornecedor;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FornecedorSaldoResponseTest {
    @Test
    public void testFornecedorSaldoResponse() {
        BigDecimal saldo = BigDecimal.valueOf(20.4);
        FornecedorSaldoResponse fornecedorSaldoResponse = Fixture.createFornecedorSaldoResponse(saldo);

        assertThat(fornecedorSaldoResponse.getSaldo()).isEqualTo(saldo);
    }
}
