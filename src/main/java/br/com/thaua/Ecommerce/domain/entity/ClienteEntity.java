package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractRelationWithUsers;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@Getter
@Setter
public class ClienteEntity extends AbstractRelationWithUsers {
    @CPF
    private String cpf;

//    TALVEZ ACRESCENTAR UM VALOR PARA MOSTRAR QUANTOS PEDIDOS ESSE USUARIO JA FEZ
    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<PedidoEntity> pedido;

}
