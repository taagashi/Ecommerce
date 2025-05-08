package br.com.thaua.Ecommerce.services.produto;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.CategoriaComponentResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.ProdutoNotFoundException;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.specifications.ProdutoSpecifications;
import br.com.thaua.Ecommerce.services.ProdutoService;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private ProdutoSpecifications produtoSpecifications;

    @Mock
    private PaginaMapper paginaMapper;

    @Mock
    private ValidationService validationService;

    @Mock
    private Map<String, String> errors;

    @BeforeEach
    public void setUp() {
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso um produto")
    @Test
    public void testBuscarProdutoSucesso() {
        Long produtoId = 3L;
        String nome = "chapeu";
        String descricao = "produto de chapeu";
        BigDecimal preco = BigDecimal.valueOf(14.2);
        Integer estoque = 5;
        Integer quantidadeDemanda = 2;
        Integer categoriasAssociadas = 9;

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(produtoId, nome, descricao, preco, estoque, List.of(), List.of(), quantidadeDemanda, null);

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoId, nome, descricao, preco, estoque, quantidadeDemanda, categoriasAssociadas);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoEntity));

        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);

        ProdutoResponse produtoResponseCompare = produtoService.buscarProduto(produtoId, errors);

        assertThat(produtoResponseCompare.getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponseCompare.getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponseCompare.getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponseCompare.getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponseCompare.getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponseCompare.getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao tentar buscar produto com id incorreto")
    @Test
    public void testBuscarProdutoError() {
        String errorMessage = "Houve um erro ao tentar buscar o produto";
        errors.put("Falha de busca", "Item não encontrado");
        Long produtoIdError = -3L;

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> produtoService.buscarProduto(produtoIdError, errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));
    }

    @DisplayName("Deve retornar com sucesso as categorias de um produto")
    @Test
    public void testExibirCategoriasDeProdutoSucesso() {
        Long produtoId = 9L;
        String nome = "cama";
        String descricao = "produto de cama";
        BigDecimal preco = BigDecimal.valueOf(200);
        int estoque = 3;
        int quantidadeDemanda = 9;

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(produtoId, nome, descricao, preco, estoque, List.of(), List.of(), quantidadeDemanda, null);

        CategoriaComponentResponse categoriaComponentResponse = Fixture.createCategoriaComponentResponse(1L, "conforto");
        List<CategoriaComponentResponse> categoriaComponentResponseList = List.of(categoriaComponentResponse);

        ProdutoCategoriaResponse produtoCategoriaResponse = Fixture.createProdutoCategoriaResponse(produtoId, nome, preco.toString(), categoriaComponentResponseList);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoEntity));
        when(produtoMapper.toProdutoCategoriaResponse(produtoEntity)).thenReturn(produtoCategoriaResponse);

        ProdutoCategoriaResponse produtoCategoriaResponseCompare = produtoService.exibirCategoriasDeProduto(produtoId, errors);

        assertThat(produtoCategoriaResponseCompare.getProdutoId()).isEqualTo(produtoCategoriaResponse.getProdutoId());
        assertThat(produtoCategoriaResponseCompare.getNome()).isEqualTo(produtoCategoriaResponse.getNome());
        assertThat(produtoCategoriaResponseCompare.getPreco()).isEqualTo(produtoCategoriaResponse.getPreco());
        assertThat(produtoCategoriaResponseCompare.getCategorias().getFirst().getCategoriaId()).isEqualTo(produtoCategoriaResponse.getCategorias().getFirst().getCategoriaId());
        assertThat(produtoCategoriaResponseCompare.getCategorias().getFirst().getNome()).isEqualTo(produtoCategoriaResponse.getCategorias().getFirst().getNome());
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao tentar exibir categorias de um produto com id incorreto")
    @Test
    public void testExibirCategoriasDeProdutoError() {
        String errorMessage = "Houve um erro ao tentar exibir categorias de um produto";
        errors.put("Falha de busca", "Item não encontrado");
        Long produtoIdError = -9L;

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> produtoService.exibirCategoriasDeProduto(produtoIdError, errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));
    }

    @DisplayName("Deve retornar com sucesso uma pagina de produtos")
    @Test
    public void testExibirProdutos() {
        BigDecimal precoMinimo = BigDecimal.valueOf(12);
        BigDecimal precoMaximo = BigDecimal.valueOf(40);

        Long produtoId = 3L;
        String produtoNome = "cama";
        String produtoDescricao = "produto de cama";
        BigDecimal produtoPreco = BigDecimal.valueOf(200);
        int produtoEstoque = 3;
        int produtoQuantidadeDemanda = 9;
        int produtoCategoriasAssociadas = 1;

        Pageable pageable = PageRequest.of(0, 1);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(produtoId, produtoNome, produtoDescricao, produtoPreco, produtoEstoque, List.of(), List.of(), produtoQuantidadeDemanda, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        Page<ProdutoEntity> produtoEntityPage = new PageImpl<>(produtoEntityList, pageable, produtoEntityList.size());


        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoId, produtoNome, produtoDescricao, produtoPreco, produtoEstoque, produtoQuantidadeDemanda, produtoCategoriasAssociadas);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Page<ProdutoResponse> produtoResponsePage = new PageImpl<>(produtoResponseList, pageable, produtoResponseList.size());

        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(produtoResponsePage.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens(produtoResponsePage.getTotalElements());
        produtoResponsePagina.setUltimaPagina(true);

        when(produtoSpecifications.buscarComFiltros(precoMinimo, precoMaximo, pageable)).thenReturn(produtoEntityPage);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);
        when(paginaMapper.toPagina(produtoResponsePage)).thenReturn(produtoResponsePagina);

        Pagina<ProdutoResponse> produtoResponsePaginaCompare = produtoService.exibirProdutos(pageable, precoMinimo, precoMaximo);

        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getProdutoId()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getProdutoId());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getNome()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getNome());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getDescricao()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getDescricao());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getPreco()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getPreco());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getEstoque()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getEstoque());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getQuantidadeDemanda()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getQuantidadeDemanda());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getCategoriasAssociadas()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getCategoriasAssociadas());
        assertThat(produtoResponsePaginaCompare.getPaginaAtual()).isEqualTo(produtoResponsePagina.getPaginaAtual());
        assertThat(produtoResponsePaginaCompare.getTotalPaginas()).isEqualTo(produtoResponsePagina.getTotalPaginas());
        assertThat(produtoResponsePaginaCompare.getItensPorPagina()).isEqualTo(produtoResponsePagina.getItensPorPagina());
        assertThat(produtoResponsePaginaCompare.getTotalItens()).isEqualTo(produtoResponsePagina.getTotalItens());
        assertThat(produtoResponsePaginaCompare.getUltimaPagina()).isEqualTo(produtoResponsePagina.getUltimaPagina());
    }
}