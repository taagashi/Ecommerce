package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoriaProdutosResponse {
    private Long categoriaId;
    private String nome;
    private String descricao;
    private List<ProdutoComponentResponse> produtos;
}
