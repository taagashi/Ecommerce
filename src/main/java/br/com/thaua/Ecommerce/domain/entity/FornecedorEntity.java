package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractDataClientAndSupplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class FornecedorEntity extends AbstractDataClientAndSupplier {
    private String cnpj;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @OneToMany(mappedBy = "fornecedor")
    private List<ProdutoEntity> produto;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity users;
}
