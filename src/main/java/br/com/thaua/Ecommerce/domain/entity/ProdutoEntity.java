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
public class ProdutoEntity extends AbstractEntity {
    @Column(unique = true)
    private String nome;

    @Column(unique = true)
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;

    @ManyToMany(mappedBy = "produtos", fetch = FetchType.EAGER)
    private List<CategoriaEntity> categorias;

    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER)
    private List<ItemPedidoEntity> itensPedidos;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private FornecedorEntity fornecedor;
}
