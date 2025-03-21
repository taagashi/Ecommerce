package br.com.thaua.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersLoginRequest {
    private String email;
    private String password;
}
