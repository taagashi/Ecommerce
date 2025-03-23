package br.com.thaua.Ecommerce.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
