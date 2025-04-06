package br.com.thaua.Ecommerce.dto.pagina;

import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class GerarPaginacao {
    public static <T> Pagina<T> gerarPaginacao(Page<T> page) {
        Pagina<T> pagina = new Pagina<>();

        pagina.setConteudo(page.getContent());
        pagina.setPaginaAtual(page.getNumber());
        pagina.setTotalPaginas(page.getTotalPages());
        pagina.setTotalItens(page.getTotalElements());
        pagina.setItensPorPagina(page.getNumberOfElements());
        pagina.setUltimaPagina(page.isLast());

        return pagina;
    }
}
