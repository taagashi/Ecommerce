package br.com.thaua.Ecommerce.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersLoginRequest {
    private String email;
    private String password;
}
