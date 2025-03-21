package br.com.thaua.Ecommerce.dto;

import br.com.thaua.Ecommerce.domain.enums.Role;
import jakarta.persistence.Enumerated;
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
