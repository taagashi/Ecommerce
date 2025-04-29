package br.com.thaua.Ecommerce.dto.users;

import lombok.Data;

@Data
public class UsersLoginRequest {
    private String email;
    private String password;
}
