package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoComponentResponse {
    private Long produtoId;
    private String nome;
    private BigDecimal preco;
    private Integer estoque;
}
