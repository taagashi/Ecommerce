package br.com.thaua.Ecommerce.dto.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsersRequestTest {
    @Test
    public void testUsersRequest() {
        String name = "melissa";
        String email = "melissa@gmail.com";
        String password = "123senha";
        String role = "CLIENTE";
        UsersRequest usersRequest = Fixture.createUserRequest(name, email, password, role);

        assertThat(usersRequest.getName()).isEqualTo(name);
        assertThat(usersRequest.getEmail()).isEqualTo(email);
        assertThat(usersRequest.getPassword()).isEqualTo(password);
        assertThat(usersRequest.getRole()).isEqualTo(role);
    }
}
