package br.com.thaua.Ecommerce.dto.produto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoResponse {
    private Long produtoId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Integer quantidadeDemanda;
    private Integer categoriasAssociadas;

}
