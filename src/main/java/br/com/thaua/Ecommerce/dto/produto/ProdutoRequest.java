package br.com.thaua.Ecommerce.dto.produto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequest {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
}
