package br.com.thaua.Ecommerce.controllers.produto;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.ProdutoController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.CategoriaComponentResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.ProdutoNotFoundException;
import br.com.thaua.Ecommerce.services.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {
    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    private Map<String, String> emptyMap;

    private Map<String, String> errors;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        errors = ConstructorErrors.returnMapErrors();
        emptyMap = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso todas as categorias de um produto cadastrado")
    @Test
    public void testExibirCategoriasProdutoSucesso() throws Exception {
        CategoriaComponentResponse categoriaComponentResponse = Fixture.createCategoriaComponentResponse(1L, "automovel");
        CategoriaComponentResponse categoriaComponentResponse2 = Fixture.createCategoriaComponentResponse(2L, "rapido");
        ProdutoCategoriaResponse produtoCategoriaResponse = Fixture.createProdutoCategoriaResponse(15L, "carro", "10000",  List.of(categoriaComponentResponse, categoriaComponentResponse2));
        Long produtoId = produtoCategoriaResponse.getProdutoId();

        when(produtoService.exibirCategoriasDeProduto(eq(produtoId), eq(emptyMap))).thenReturn(produtoCategoriaResponse);

        mockMvc.perform(get("/api/v1/produtos/{produtoId}/categorias/list", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoCategoriaResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoCategoriaResponse.getNome()))
                .andExpect(jsonPath("$.preco").value(produtoCategoriaResponse.getPreco()))
                .andExpect(jsonPath("$.categorias").isArray())
                .andExpect(jsonPath("$.categorias[0].categoriaId").value(categoriaComponentResponse.getCategoriaId()))
                .andExpect(jsonPath("$.categorias[0].nome").value(categoriaComponentResponse.getNome()))
                .andExpect(jsonPath("$.categorias[1].categoriaId").value(categoriaComponentResponse2.getCategoriaId()))
                .andExpect(jsonPath("$.categorias[1].nome").value(categoriaComponentResponse2.getNome()));

        verify(produtoService,times(1)).exibirCategoriasDeProduto(eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar um ProdutoNotFoundException ao tentar exibir categorias de um produto que nao existe")
    @Test
    public void testExibirCategoriasProdutoError() throws Exception {
        Long produtoIdError = 11L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar exibir categorias de um produto");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        when(produtoService.exibirCategoriasDeProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(get("/api/v1/produtos/{produtoId}/categorias/list", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(produtoService, times(1)).exibirCategoriasDeProduto(eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso todos os produtos cadastrados")
    @Test
    public void testExibirProdutosSucesso() throws Exception {
        BigDecimal precoMinimo = BigDecimal.valueOf(15.25);
        BigDecimal precoMaximo = BigDecimal.valueOf(25.60);
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(23L, "patinete", "produto de patinete", BigDecimal.valueOf(20.56), 15, 1, 4);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);
        Pageable pageable = PageRequest.of(0, 1);
        Page<ProdutoResponse> produtoResponsePage = new PageImpl(produtoResponseList, pageable, produtoResponseList.size());
        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(produtoResponsePage.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens((long) produtoResponseList.size());
        produtoResponsePagina.setUltimaPagina(true);

        when(produtoService.exibirProdutos(eq(pageable), eq(precoMinimo), eq(precoMaximo))).thenReturn(produtoResponsePagina);

        mockMvc.perform(get("/api/v1/produtos/list")
                .param("page", "0")
                .param("size", "1")
                .param("min", precoMinimo.toString())
                .param("max", precoMaximo.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.conteudo[0].nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.conteudo[0].descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.conteudo[0].preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.conteudo[0].estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.conteudo[0].quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.conteudo[0].categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(produtoService, times(1)).exibirProdutos(eq(pageable), eq(precoMinimo), eq(precoMaximo));
    }

    @DisplayName("Deve retornar com sucesso um produto cadastrado")
    @Test
    public void testBuscarProdutoSucesso() throws Exception {
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(1L, "relogio", "produto de relogio", BigDecimal.valueOf(500), 2, 2, 10);
        Long produtoId = produtoResponse.getProdutoId();

        when(produtoService.buscarProduto(eq(produtoId), eq(emptyMap))).thenReturn(produtoResponse);

        mockMvc.perform(get("/api/v1/produtos/{produtoId}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(produtoService, times(1)).buscarProduto(eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar um ProdutoNotFoundException ao tentar buscar um produto que nao existe")
    @Test
    public void testBuscarProdutoError() throws Exception {
        Long produtoIdError = 2L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar buscar o produto");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        when(produtoService.buscarProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(get("/api/v1/produtos/{produtoId}", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(produtoService, times(1)).buscarProduto(eq(produtoIdError), eq(emptyMap));

    }
}
