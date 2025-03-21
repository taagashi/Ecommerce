package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractCommonData;
import br.com.thaua.Ecommerce.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UsersEntity extends AbstractCommonData {
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private AdminEntity admin;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private ClienteEntity cliente;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private FornecedorEntity fornecedor;
}
