package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaProdutosResponse {
    private Long categoriaId;
    private String nome;
    private String descricao;
    private List<ProdutoComponentResponse> produtos;
}
