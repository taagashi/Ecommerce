package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "0", message = "O estoque não pode ser negativo")
    private BigDecimal preco;

    @DecimalMin(value = "0", message = "O estoque não pode ser negativo")
    private Integer estoque;

    @ManyToMany(mappedBy = "produtos", fetch = FetchType.EAGER)
    private List<CategoriaEntity> categorias;

    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<ItemPedidoEntity> itensPedidos;

    private int quantidadeDemanda;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private FornecedorEntity fornecedor;
}
