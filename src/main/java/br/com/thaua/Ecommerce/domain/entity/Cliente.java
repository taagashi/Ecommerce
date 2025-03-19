package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractDataClientAndSupplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Cliente extends AbstractDataClientAndSupplier {
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    @OneToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
