package br.com.thaua.Ecommerce.dto.pagina;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pagina <T> {
    private List<T> conteudo;
    private Integer paginaAtual;
    private Integer totalPaginas;
    private Integer itensPorPagina;
    private Long totalItens;
    private Boolean ultimaPagina;
}
