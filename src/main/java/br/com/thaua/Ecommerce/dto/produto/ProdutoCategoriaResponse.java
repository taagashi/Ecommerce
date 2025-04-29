package br.com.thaua.Ecommerce.dto.produto;

import lombok.Data;

import java.util.List;

@Data
public class ProdutoCategoriaResponse {
    private Long produtoId;
    private String nome;
    private String preco;
    private List<CategoriaComponentResponse> categorias;

}
