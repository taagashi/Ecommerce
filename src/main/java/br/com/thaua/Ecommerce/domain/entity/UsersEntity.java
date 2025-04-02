package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UsersEntity extends AbstractEntity {
    private String name;
    private String email;
    private String telefone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private AdminEntity admin;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ClienteEntity cliente;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private FornecedorEntity fornecedor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;
}
