package br.com.thaua.Ecommerce.dto.admin;

import br.com.thaua.Ecommerce.Fixture;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdminResponseTest {

    @Test
    public void testAdminResponse() {
        Long id = 1L;
        String name = "jonas";
        String email = "jonas@gmail.com";
        int contasBanidas = 5;
        LocalDateTime ultimoAcesso = LocalDateTime.now();

        AdminResponse adminResponse = Fixture.createAdminResponse(id, name, email, contasBanidas, ultimoAcesso);

        assertThat(adminResponse.getId()).isEqualTo(id);
        assertThat(adminResponse.getName()).isEqualTo(name);
        assertThat(adminResponse.getEmail()).isEqualTo(email);
        assertThat(adminResponse.getContasBanidas()).isEqualTo(contasBanidas);
        assertThat(adminResponse.getUltimoAcesso()).isEqualTo(ultimoAcesso);
    }
}
