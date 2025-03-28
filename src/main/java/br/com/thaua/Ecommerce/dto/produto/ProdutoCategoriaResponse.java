package br.com.thaua.Ecommerce.dto.produto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProdutoCategoriaResponse {
    private Long produtoId;
    private String nome;
    private String preco;
    private List<CategoriaComponentResponse> categorias;

}
