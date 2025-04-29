package br.com.thaua.Ecommerce.dto.users;

import lombok.Data;

@Data
public class UserRequestGenerateNewPassword {
    private String email;
    private String newPassword;
    private int code;
}
