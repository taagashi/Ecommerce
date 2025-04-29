package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoComponentResponse {
    private Long produtoId;
    private String nome;
    private BigDecimal preco;
    private Integer estoque;
}
