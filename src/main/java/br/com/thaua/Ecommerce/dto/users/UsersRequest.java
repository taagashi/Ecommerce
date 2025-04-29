package br.com.thaua.Ecommerce.dto.users;

import lombok.Data;

@Data
public class UsersRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
