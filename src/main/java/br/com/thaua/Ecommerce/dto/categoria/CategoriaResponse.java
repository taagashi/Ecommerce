package br.com.thaua.Ecommerce.dto.categoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer produtosAssociados;
}
