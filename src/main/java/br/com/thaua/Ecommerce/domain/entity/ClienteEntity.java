package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractDataClientAndSupplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ClienteEntity extends AbstractDataClientAndSupplier {
    @OneToMany(mappedBy = "cliente")
    private List<PedidoEntity> pedido;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity users;
}
