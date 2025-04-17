package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class ItemPedidoEntity extends AbstractEntity {
    private Integer quantidade;
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name="pedido_id")
    private PedidoEntity pedido;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="produto_id")
    private ProdutoEntity produto;

    @Enumerated(EnumType.STRING)
    private StatusItemPedido statusItemPedido;
}
