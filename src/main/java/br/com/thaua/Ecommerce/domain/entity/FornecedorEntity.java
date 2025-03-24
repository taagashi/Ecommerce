package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractDataClientAndSupplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;

@Entity
@Getter
@Setter
public class FornecedorEntity extends AbstractDataClientAndSupplier {
    @CNPJ
    private String cnpj;

    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.EAGER)
    private List<ProdutoEntity> produto;

}
