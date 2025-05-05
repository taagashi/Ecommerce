package br.com.thaua.Ecommerce.dto.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserRequestGenerateCodeTest {
    @Test
    public void testUserRequestGenerateCode() {
        String email = "kris@gmail.com";
        UserRequestGenerateCode userRequestGenerateCode = Fixture.createUserRequestGenerateCode(email);

        assertThat(userRequestGenerateCode.getEmail()).isEqualTo(email);
    }
}
