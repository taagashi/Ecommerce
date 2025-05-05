package br.com.thaua.Ecommerce.dto.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.users.UsersLoginRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsersLoginRequestTest {
    @Test
    public void testUsersLoginRequest() {
        String email = "luz@gmail.com";
        String password = "123senha";

        UsersLoginRequest usersLoginRequest = Fixture.createUsersLoginRequest(email, password);

        assertThat(usersLoginRequest.getEmail()).isEqualTo(email);
        assertThat(usersLoginRequest.getPassword()).isEqualTo(password);
    }
}
