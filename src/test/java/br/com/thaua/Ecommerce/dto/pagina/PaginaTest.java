package br.com.thaua.Ecommerce.dto.pagina;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PaginaTest {
    @Test
    public void testPagina() {
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(10L, "garrafa", "produto de garrafa", BigDecimal.valueOf(23), 12, 2, 4);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Integer paginaAtual = 0;
        Integer totalPaginas = 2;
        Integer itensPorPagina = 10;
        Long totalItens = 20L;
        Boolean ultimaPagina = true;
        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(paginaAtual);
        produtoResponsePagina.setTotalPaginas(totalPaginas);
        produtoResponsePagina.setItensPorPagina(itensPorPagina);
        produtoResponsePagina.setTotalItens(totalItens);
        produtoResponsePagina.setUltimaPagina(ultimaPagina);

        assertThat(produtoResponsePagina.getConteudo().size()).isEqualTo(1);
        assertThat(produtoResponsePagina.getConteudo().getFirst().getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponsePagina.getConteudo().getFirst().getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
        assertThat(produtoResponsePagina.getPaginaAtual()).isEqualTo(paginaAtual);
        assertThat(produtoResponsePagina.getTotalPaginas()).isEqualTo(totalPaginas);
        assertThat(produtoResponsePagina.getItensPorPagina()).isEqualTo(itensPorPagina);
        assertThat(produtoResponsePagina.getTotalItens()).isEqualTo(totalItens);
        assertThat(produtoResponsePagina.getUltimaPagina()).isEqualTo(ultimaPagina);
    }
}
