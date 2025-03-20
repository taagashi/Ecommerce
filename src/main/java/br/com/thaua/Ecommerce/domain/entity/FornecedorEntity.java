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
    @OneToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity enderecoEntity;

    @OneToMany(mappedBy = "fornecedor")
    private List<Produto> produtos;

    @OneToOne(mappedBy = "fornecedor")
    private Users users;
}
