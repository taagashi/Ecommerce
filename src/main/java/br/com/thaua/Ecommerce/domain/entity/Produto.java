package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Produto extends AbstractEntity {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;

    @ManyToMany(mappedBy = "produtos")
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedidos;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;
}
