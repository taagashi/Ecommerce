package br.com.thaua.Ecommerce.services.categoria;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.exceptions.CategoriaNotFoundException;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.services.CategoriaService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Mock
    private PaginaMapper paginaMapper;

    @Mock
    private ValidationService validationService;

    private Map<String, String> errors;


    @BeforeEach
    public void setUp() {
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso pagina com categorias responses")
    @Test
    public void testExibirCategoriasSucesso() {
        Pageable pageable = PageRequest.of(0, 1);

        Long id = 1L;
        String nome = "hospitalar";
        String descricao = "categoria para produtos hospitalares";

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(id, nome, descricao, List.of());
        List<CategoriaEntity> categoriaEntityList = List.of(categoriaEntity);

        Page<CategoriaEntity> categoriaEntityPage = new PageImpl<>(categoriaEntityList, pageable, categoriaEntityList.size());

        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(id, nome, descricao, 5);
        List<CategoriaResponse> categoriaResponseList = List.of(categoriaResponse);

        Page<CategoriaResponse> categoriaResponsePage = new PageImpl<>(categoriaResponseList, pageable, categoriaResponseList.size());

        Pagina<CategoriaResponse> categoriaResponsePagina1 = new Pagina<>();
        categoriaResponsePagina1.setConteudo(categoriaResponseList);
        categoriaResponsePagina1.setPaginaAtual(pageable.getPageNumber());
        categoriaResponsePagina1.setItensPorPagina(pageable.getPageSize());
        categoriaResponsePagina1.setTotalPaginas(categoriaResponsePage.getTotalPages());
        categoriaResponsePagina1.setUltimaPagina(true);
        categoriaResponsePagina1.setTotalItens(categoriaResponsePage.getTotalElements());

        when(categoriaRepository.findAll(pageable)).thenReturn(categoriaEntityPage);
        when(categoriaMapper.toResponse(any(CategoriaEntity.class))).thenReturn(categoriaResponse);
        when(paginaMapper.toPagina(categoriaResponsePage)).thenReturn(categoriaResponsePagina1);

        Pagina<CategoriaResponse> categoriaResponsePagina = categoriaService.exibirCategorias(pageable);

        assertThat(categoriaResponsePagina1.getConteudo().getFirst().getId()).isEqualTo(categoriaResponsePagina.getConteudo().getFirst().getId());
        assertThat(categoriaResponsePagina1.getConteudo().getFirst().getNome()).isEqualTo(categoriaResponsePagina.getConteudo().getFirst().getNome());
        assertThat(categoriaResponsePagina1.getConteudo().getFirst().getDescricao()).isEqualTo(categoriaResponsePagina.getConteudo().getFirst().getDescricao());
        assertThat(categoriaResponsePagina1.getConteudo().getFirst().getProdutosAssociados()).isEqualTo(categoriaResponsePagina.getConteudo().getFirst().getProdutosAssociados());
        assertThat(categoriaResponsePagina1.getPaginaAtual()).isEqualTo(categoriaResponsePagina.getPaginaAtual());
        assertThat(categoriaResponsePagina1.getTotalPaginas()).isEqualTo(categoriaResponsePagina.getTotalPaginas());
        assertThat(categoriaResponsePagina1.getItensPorPagina()).isEqualTo(categoriaResponsePagina.getItensPorPagina());
        assertThat(categoriaResponsePagina1.getTotalItens()).isEqualTo(categoriaResponsePagina.getTotalItens());
        assertThat(categoriaResponsePagina1.getUltimaPagina()).isEqualTo(categoriaResponsePagina.getUltimaPagina());

        verify(categoriaMapper, times(1)).toResponse(categoriaEntity);
        verify(categoriaRepository, times(1)).findAll(pageable);
        verify(paginaMapper, times(1)).toPagina(categoriaResponsePage);
    }

    @DisplayName("Deve retornar com sucesso categoria response")
    @Test
    public void testExibirCategoriaSucesso() {
        Long categoriaId = 2L;
        String nome = "computadores";
        String descricao = "categoria para computadores";

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(categoriaId, nome, descricao, List.of());
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(categoriaId, nome, descricao, 4);

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaEntity));

        when(categoriaMapper.toResponse(categoriaEntity)).thenReturn(categoriaResponse);

        CategoriaResponse categoriaResponseCompare = categoriaService.exibirCategoria(categoriaId, errors);

        assertThat(categoriaResponse.getId()).isEqualTo(categoriaResponseCompare.getId());
        assertThat(categoriaResponse.getNome()).isEqualTo(categoriaResponseCompare.getNome());
        assertThat(categoriaResponse.getDescricao()).isEqualTo(categoriaResponseCompare.getDescricao());
        assertThat(categoriaResponse.getProdutosAssociados()).isEqualTo(categoriaResponseCompare.getProdutosAssociados());

        verify(categoriaRepository, times(1)).findById(categoriaId);
        verify(categoriaMapper, times(1)).toResponse(categoriaEntity);
    }

    @DisplayName("Deve retornar CategoriaNotFoundException ao tentar buscar pro categoria com id errado")
    @Test
    public void testExibirCategoriaError() {
        Long categoriaIdError = 6L;
        String errorMessage = "Houve um erro ao tentar exibir categoria";
        errors.put("Falha de busca", "Item não encontrado");

        when(categoriaRepository.findById(categoriaIdError)).thenReturn(Optional.empty());

        doThrow(new CategoriaNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, CategoriaNotFoundException.class, errors);

        CategoriaNotFoundException categoriaNotFoundException = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.exibirCategoria(categoriaIdError, errors));

        assertThat(categoriaNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(categoriaNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));

        verify(categoriaRepository, times(1)).findById(categoriaIdError);
        verify(validationService, times(1)).validarExistenciaEntidade(null, errors, "Categoria");
        verify(validationService, times(1)).analisarException(errorMessage, CategoriaNotFoundException.class, errors);
    }

    @DisplayName("Deve retornar com sucesso lista de produtos por categoria")
    @Test
    public void testListarProdutoPorCategoriaSucesso() {
        Long categoriaId = 2L;

        String nome = "jogos";
        String descricao = "categoria para jogos";

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(categoriaId, nome, descricao, List.of());

        ProdutoComponentResponse produtoComponentResponse = Fixture.createProdutoComponentResponse(1L, "mincraft", BigDecimal.valueOf(34), 4);
        List<ProdutoComponentResponse> produtoComponentResponseList = List.of(produtoComponentResponse);

        CategoriaProdutosResponse categoriaProdutosResponse = Fixture.createCategoriaProdutosResponse(categoriaId, nome, descricao, produtoComponentResponseList);

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaEntity));
        when(categoriaMapper.toCategoriaProdutosResponse(categoriaEntity)).thenReturn(categoriaProdutosResponse);

        CategoriaProdutosResponse categoriaProdutosResponseCompare = categoriaService.listarProdutosPorCategoria(categoriaId, errors);

        assertThat(categoriaProdutosResponseCompare.getCategoriaId()).isEqualTo(categoriaProdutosResponse.getCategoriaId());
        assertThat(categoriaProdutosResponseCompare.getNome()).isEqualTo(categoriaProdutosResponse.getNome());
        assertThat(categoriaProdutosResponseCompare.getDescricao()).isEqualTo(categoriaProdutosResponse.getDescricao());
        assertThat(categoriaProdutosResponseCompare.getProdutos().getFirst().getProdutoId()).isEqualTo(categoriaProdutosResponse.getProdutos().getFirst().getProdutoId());
        assertThat(categoriaProdutosResponseCompare.getProdutos().getFirst().getNome()).isEqualTo(categoriaProdutosResponse.getProdutos().getFirst().getNome());
        assertThat(categoriaProdutosResponseCompare.getProdutos().getFirst().getPreco()).isEqualTo(categoriaProdutosResponse.getProdutos().getFirst().getPreco());
        assertThat(categoriaProdutosResponseCompare.getProdutos().getFirst().getEstoque()).isEqualTo(categoriaProdutosResponse.getProdutos().getFirst().getEstoque());

        verify(categoriaRepository, times(1)).findById(categoriaId);
        verify(categoriaMapper, times(1)).toCategoriaProdutosResponse(categoriaEntity);
    }

    @DisplayName("Deve retornar CategoriaNotFoundException após buscar por categoria errada")
    @Test
    public void testListarProdutoPorCategoriaError() {
        String errorMessage = "Houve um erro ao tentar listar produtos de uma categoria";
        errors.put("Falha de busca", "Item não encontrado");
        Long categoriaIdError = -5L;

        doThrow(new CategoriaNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, CategoriaNotFoundException.class, errors);

        CategoriaNotFoundException categoriaNotFoundException = assertThrows(CategoriaNotFoundException.class, () -> categoriaService.listarProdutosPorCategoria(categoriaIdError, errors));

        assertThat(categoriaNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(categoriaNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));

        verify(categoriaRepository, times(1)).findById(categoriaIdError);
        verify(validationService, times(1)).validarExistenciaEntidade(null, errors, "Categoria");
        verify(validationService, times(1)).analisarException(errorMessage, CategoriaNotFoundException.class, errors);
    }
}
