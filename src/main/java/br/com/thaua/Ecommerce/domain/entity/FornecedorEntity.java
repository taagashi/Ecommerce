package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractRelationWithUsers;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;

@Entity
@Getter
@Setter
public class FornecedorEntity extends AbstractRelationWithUsers {
//    @CNPJ(message = "CNPJ invalido")
//    @Column(unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.EAGER)
    private List<ProdutoEntity> produto;

}
