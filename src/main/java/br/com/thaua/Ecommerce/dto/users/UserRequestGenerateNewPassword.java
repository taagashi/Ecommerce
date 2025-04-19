package br.com.thaua.Ecommerce.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestGenerateNewPassword {
    private String email;
    private String newPassword;
    private int code;
}
