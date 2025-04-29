package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer produtosAssociados;
}
