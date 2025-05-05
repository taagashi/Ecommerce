package br.com.thaua.Ecommerce.dto.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.users.UsersResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsersResponseTest {
    @Test
    public void testUsersResponse() {
        String name = "sally";
        String email = "sally@gmail.com";
        UsersResponse usersResponse = Fixture.createUserResponse(name, email);

        assertThat(usersResponse.getName()).isEqualTo(name);
        assertThat(usersResponse.getEmail()).isEqualTo(email);
    }
}
