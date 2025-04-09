package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PaginaMapper {

    default <T> Pagina<T> toPagina(Page<T> page) {
        Pagina<T> pagina = new Pagina<>();
        pagina.setConteudo(page.getContent());
        pagina.setPaginaAtual(page.getNumber());
        pagina.setTotalPaginas(page.getTotalPages());
        pagina.setTotalItens(page.getTotalElements());
        pagina.setItensPorPagina(page.getSize());
        pagina.setUltimaPagina(page.isLast());

        return pagina;
    }
}
