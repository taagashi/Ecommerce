package br.com.thaua.Ecommerce.dto.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserRequestGenerateNewPasswordTest {
    @Test
    public void testUserRequestGenerateNewPassword() {
        String email = "panem@gmail.com";
        String newPassword = "123senha";
        int code = 12331232;
        UserRequestGenerateNewPassword userRequestGenerateNewPassword = Fixture.createUserRequestGenerateNewPassword(email, newPassword, code);

        assertThat(userRequestGenerateNewPassword.getEmail()).isEqualTo(email);
        assertThat(userRequestGenerateNewPassword.getNewPassword()).isEqualTo(newPassword);
        assertThat(userRequestGenerateNewPassword.getCode()).isEqualTo(code);
    }
}
