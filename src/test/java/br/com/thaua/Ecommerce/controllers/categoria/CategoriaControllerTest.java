package br.com.thaua.Ecommerce.controllers.categoria;

import br.com.thaua.Ecommerce.controllers.CategoriaController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.exceptions.CategoriaNotFoundException;
import br.com.thaua.Ecommerce.services.CategoriaService;
import br.com.thaua.Ecommerce.Fixture;
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
public class CategoriaControllerTest {
    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaService categoriaService;

    private MockMvc mockMvc;

    private Map<String, String> errors;

    private Map<String, String> emptyMap;

    @BeforeEach
    public void setUp() {
            mockMvc = MockMvcBuilders.standaloneSetup(categoriaController)
                    .setControllerAdvice(new ExceptionHandlerClass())
                    .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                    .build();
            errors = ConstructorErrors.returnMapErrors();
            emptyMap = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso todas as categorias cadastradas")
    @Test
    public void testExibirCategoriasSucesso() throws Exception {
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(1L, "carro", "categoria de carro", 2);
        CategoriaResponse categoriaResponse2 = Fixture.createCategoriaResponse(2L, "bicicleta", "categoria de bicicleta", 0);

        List<CategoriaResponse> categorias = List.of(categoriaResponse, categoriaResponse2);

        Pageable pageableCategoria = PageRequest.of(0, 2);
        Page<CategoriaResponse> pageCategorias = new PageImpl<>(categorias, pageableCategoria, categorias.size());

        Pagina<CategoriaResponse> paginaCategoria = new Pagina<>();
        paginaCategoria.setConteudo(categorias);
        paginaCategoria.setPaginaAtual(pageableCategoria.getPageNumber());
        paginaCategoria.setTotalPaginas(pageCategorias.getTotalPages());
        paginaCategoria.setItensPorPagina(pageableCategoria.getPageSize());
        paginaCategoria.setTotalItens((long) categorias.size());
        paginaCategoria.setUltimaPagina(true);

        when(categoriaService.exibirCategorias(eq(pageableCategoria))).thenReturn(paginaCategoria);

        mockMvc.perform(get("/api/v1/categorias/list")
                        .param("page", String.valueOf(pageableCategoria.getPageNumber()))
                        .param("size", String.valueOf(pageableCategoria.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo.length()").value(2))
                .andExpect(jsonPath("$.conteudo[0].id").value(categoriaResponse.getId()))
                .andExpect(jsonPath("$.conteudo[0].nome").value(categoriaResponse.getNome()))
                .andExpect(jsonPath("$.conteudo[0].descricao").value(categoriaResponse.getDescricao()))
                .andExpect(jsonPath("$.conteudo[0].produtosAssociados").value(categoriaResponse.getProdutosAssociados()))
                .andExpect(jsonPath("$.conteudo[1].id").value(categoriaResponse2.getId()))
                .andExpect(jsonPath("$.conteudo[1].nome").value(categoriaResponse2.getNome()))
                .andExpect(jsonPath("$.conteudo[1].descricao").value(categoriaResponse2.getDescricao()))
                .andExpect(jsonPath("$.conteudo[1].produtosAssociados").value(categoriaResponse2.getProdutosAssociados()));

        verify(categoriaService, times(1)).exibirCategorias(eq(pageableCategoria));

    }

    @DisplayName("Deve retorna com sucesso uma categoria especifica")
    @Test
    public void testExibirCategoriaSucesso() throws Exception {
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(10L, "camisa", "categoria de camisa", 10);
        Long categoriaId = categoriaResponse.getId();

        when(categoriaService.exibirCategoria(eq(categoriaId), eq(emptyMap))).thenReturn(categoriaResponse);

        mockMvc.perform(get("/api/v1/categorias/{categoriaId}/list", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoriaId))
                .andExpect(jsonPath("$.nome").value(categoriaResponse.getNome()))
                .andExpect(jsonPath("$.produtosAssociados").value(categoriaResponse.getProdutosAssociados()))
                .andExpect(jsonPath("$.descricao").value(categoriaResponse.getDescricao()));

        verify(categoriaService, times(1)).exibirCategoria(eq(categoriaId), eq(emptyMap));
    }

    @DisplayName("Deve retornar uma exception CategoriaNotFoundException ao buscar uma cateogira especifica")
    @Test
    public void testExibirCategoriaError() throws Exception {
        Long categoriaIdError = 2L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar exibir categoria");
        CategoriaNotFoundException categoriaNotFoundException = new CategoriaNotFoundException(errorMessage, errors);

        when(categoriaService.exibirCategoria(eq(categoriaIdError), eq(emptyMap))).thenThrow(categoriaNotFoundException);

        mockMvc.perform(get("/api/v1/categorias/{categoriaId}/list", categoriaIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors.['Falha de busca']").value(errors.get("Falha de busca")));

        verify(categoriaService, times(1)).exibirCategoria(categoriaIdError, emptyMap);

    }

    @DisplayName("Deve retornar com sucesso os produtos de uma categoria")
    @Test
    public void testListarProdutosPorCategoriaSucesso() throws Exception {
        ProdutoComponentResponse produtoComponentResponse = Fixture.createProdutoComponentResponse(1L, "relogio", BigDecimal.valueOf(40023.33), 200);
        ProdutoComponentResponse produtoComponentResponse2 = Fixture.createProdutoComponentResponse(2L, "tenis", BigDecimal.valueOf(200.255), 20);
        CategoriaProdutosResponse categoriaProdutosResponse = Fixture.createCategoriaProdutosResponse(1L, "importados", "categoria para produtos importados", List.of(produtoComponentResponse, produtoComponentResponse2));
        Long categoriaId = categoriaProdutosResponse.getCategoriaId();

        when(categoriaService.listarProdutosPorCategoria(eq(categoriaId), eq(emptyMap))).thenReturn(categoriaProdutosResponse);

        mockMvc.perform(get("/api/v1/categorias/{categoriaId}/produtos/list", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaId").value(categoriaId))
                .andExpect(jsonPath("$.nome").value(categoriaProdutosResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(categoriaProdutosResponse.getDescricao()))
                .andExpect(jsonPath("$.produtos").isArray())
                .andExpect(jsonPath("$.produtos[0].produtoId").value(produtoComponentResponse.getProdutoId()))
                .andExpect(jsonPath("$.produtos[0].nome").value(produtoComponentResponse.getNome()))
                .andExpect(jsonPath("$.produtos[0].preco").value(produtoComponentResponse.getPreco()))
                .andExpect(jsonPath("$.produtos[0].estoque").value(produtoComponentResponse.getEstoque()))
                .andExpect(jsonPath("$.produtos[1].produtoId").value(produtoComponentResponse2.getProdutoId()))
                .andExpect(jsonPath("$.produtos[1].nome").value(produtoComponentResponse2.getNome()))
                .andExpect(jsonPath("$.produtos[1].preco").value(produtoComponentResponse2.getPreco()))
                .andExpect(jsonPath("$.produtos[1].estoque").value(produtoComponentResponse2.getEstoque()));

        verify(categoriaService, times(1)).listarProdutosPorCategoria(categoriaId, emptyMap);
    }

    @DisplayName("Deve retornar uma exception CategoriaNotFoundException ao buscar os produtos de uma categoria")
    @Test
    public void testListarProdutosPorCategoriaError() throws Exception {
        Long categoriaIdError = 10L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar listar produtos de uma categoria");
        CategoriaNotFoundException categoriaNotFoundException = new CategoriaNotFoundException(errorMessage, errors);

        when(categoriaService.listarProdutosPorCategoria(categoriaIdError, emptyMap)).thenThrow(categoriaNotFoundException);

        mockMvc.perform(get("/api/v1/categorias/{categoriaId}/produtos/list", categoriaIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors.['Falha de busca']").value(errors.get("Falha de busca")));

        verify(categoriaService, times(1)).listarProdutosPorCategoria(categoriaIdError, emptyMap);
    }
}
