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

    @OneToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @OneToOne
    @JoinColumn(name = "fornecedor_id")
    private FornecedorEntity fornecedor;
}
