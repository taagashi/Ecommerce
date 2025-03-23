package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractDataClientAndSupplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@Getter
@Setter
public class ClienteEntity extends AbstractDataClientAndSupplier {
    @CPF
    private String cpf;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<PedidoEntity> pedido;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

}
