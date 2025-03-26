package br.com.thaua.Ecommerce.dto.produto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponse {
    private Long produtoId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Integer quantidadePedidos;
    private Integer categoriasAssociadas;

}
