package br.com.thaua.Ecommerce.services.fornecedor;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorSaldoResponse;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.*;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.mappers.ItemPedidoMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.FornecedorService;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FornecedorServiceTest {
    @InjectMocks
    private FornecedorService fornecedorService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private PaginaMapper paginaMapper;

    @Mock
    private ItemPedidoMapper itemPedidoMapper;

    @Mock
    private ExtractTypeUserContextHolder extractTypeUserContextHolder;

    private Map<String, String> errors;

    @BeforeEach
    public void setUp() {
        errors = ConstructorErrors.returnMapErrors();
    }

    @Test
    public void testAtualizarCNPJeTelefoneSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(1L, "antonio", "antonio@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest = Fixture.createFornecedorCNPJTelefoneRequest("0000.0000.0000/0000", usersEntity.getTelefone());
        FornecedorResponse fornecedorResponse = Fixture.createFornecedorResponse(1L, usersEntity.getName(), usersEntity.getEmail(), fornecedorCNPJTelefoneRequest.getTelefone(), fornecedorCNPJTelefoneRequest.getCnpj());

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        when(fornecedorMapper.fornecedorToResponse(any(FornecedorEntity.class))).thenReturn(fornecedorResponse);

        FornecedorResponse fornecedorResponseCompare = fornecedorService.atualizarCNPJeTelefone(fornecedorCNPJTelefoneRequest);

        assertThat(fornecedorResponseCompare.getId()).isEqualTo(fornecedorResponse.getId());
        assertThat(fornecedorResponseCompare.getName()).isEqualTo(fornecedorResponse.getName());
        assertThat(fornecedorResponseCompare.getEmail()).isEqualTo(fornecedorResponse.getEmail());
        assertThat(fornecedorResponseCompare.getTelefone()).isEqualTo(fornecedorResponse.getTelefone());
        assertThat(fornecedorResponseCompare.getCnpj()).isEqualTo(fornecedorResponse.getCnpj());
    }

    @Test
    public void testCadastrarProdutoSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(1L, "kleberson", "kleberson@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoRequest produtoRequest = Fixture.createProdutoRequest("tenis", "produto de tenis", BigDecimal.valueOf(34), 4);
        List<ProdutoRequest> produtoRequestList = List.of(produtoRequest);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, produtoRequest.getNome(), produtoRequest.getDescricao(), produtoRequest.getPreco(), produtoRequest.getEstoque(), null, null, 4, usersEntity.getFornecedor());
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoMapper.produtoRequestToProdutoEntity(produtoRequestList)).thenReturn(produtoEntityList);
        when(produtoRepository.saveAll(produtoEntityList)).thenReturn(produtoEntityList);
        when(produtoMapper.produtoToResponseList(produtoEntityList)).thenReturn(produtoResponseList);

        List<ProdutoResponse> produtoResponseCompare = fornecedorService.cadastrarProduto(produtoRequestList, errors);

        assertThat(produtoResponseCompare.getFirst().getProdutoId()).isEqualTo(produtoResponseList.getFirst().getProdutoId());
        assertThat(produtoResponseCompare.getFirst().getNome()).isEqualTo(produtoResponseList.getFirst().getNome());
        assertThat(produtoResponseCompare.getFirst().getDescricao()).isEqualTo(produtoResponseList.getFirst().getDescricao());
        assertThat(produtoResponseCompare.getFirst().getPreco()).isEqualTo(produtoResponseList.getFirst().getPreco());
        assertThat(produtoResponseCompare.getFirst().getEstoque()).isEqualTo(produtoResponseList.getFirst().getEstoque());
        assertThat(produtoResponseCompare.getFirst().getQuantidadeDemanda()).isEqualTo(produtoResponseList.getFirst().getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getFirst().getCategoriasAssociadas()).isEqualTo(produtoResponseList.getFirst().getCategoriasAssociadas());
    }

    @Test
    public void testCadastrarProdutoError() {
        UsersEntity usersEntity = Fixture.createUsersEntity(1L, "kleberson", "kleberson@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoRequest produtoRequest = Fixture.createProdutoRequest("bota", "produto de bota", BigDecimal.valueOf(21), 5);
        List<ProdutoRequest> produtoRequestList = List.of(produtoRequest);

        String errorMessage = usersEntity.getName() + ", houve um erro ao tentar cadastrar o produto";
        errors.put("CNPJ", "CNPJ está não cadastrado");
        errors.put("Telefone", "Telefone não cadastrado");
        errors.put("Endereço", "Endereço não cadastrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new CadastrarProdutoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, CadastrarProdutoException.class, errors);

        CadastrarProdutoException cadastrarProdutoException = assertThrows(CadastrarProdutoException.class, () -> fornecedorService.cadastrarProduto(produtoRequestList, errors));

        assertThat(cadastrarProdutoException.getMessage()).isEqualTo(errorMessage);
        assertThat(cadastrarProdutoException.getFields().get("CNPJ")).isEqualTo(errors.get("CNPJ"));
        assertThat(cadastrarProdutoException.getFields().get("Telefone")).isEqualTo(errors.get("Telefone"));assertThat(cadastrarProdutoException.getFields().get("Endereço")).isEqualTo(errors.get("Endereço"));
    }

    @Test
    public void testExibirProdutosSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(1L, "junior", "junior@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        Pageable pageable = PageRequest.of(0, 1);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "computador", "produto de computador", BigDecimal.valueOf(13244), 5, null, null, 5, usersEntity.getFornecedor());
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);

        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Page<ProdutoEntity> produtoEntityPage = new PageImpl<>(produtoEntityList, pageable, produtoEntityList.size());

        Page<ProdutoResponse> produtoResponsePage = new PageImpl<>(produtoResponseList, pageable, produtoResponseList.size());

        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(produtoResponsePage.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens(produtoResponsePagina.getTotalItens());
        produtoResponsePagina.setUltimaPagina(true);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);
        when(produtoRepository.findAllByFornecedorId(usersEntity.getId(), pageable)).thenReturn(produtoEntityPage);
        when(paginaMapper.toPagina(produtoResponsePage)).thenReturn(produtoResponsePagina);

        Pagina<ProdutoResponse> produtoResponsePaginaCompare = fornecedorService.exibirProdutos(pageable);

        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getProdutoId()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getProdutoId());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getNome()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getNome());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getDescricao()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getDescricao());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getPreco()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getPreco());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getEstoque()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getEstoque());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getQuantidadeDemanda()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getQuantidadeDemanda());
        assertThat(produtoResponsePaginaCompare.getConteudo().getFirst().getCategoriasAssociadas()).isEqualTo(produtoResponsePagina.getConteudo().getFirst().getCategoriasAssociadas());
    }

    @Test
    public void testBuscarProdutoSucesso() {
        Long produtoId = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(5L, "mari", "mari@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "bicicleta", "produto de bicicleta", BigDecimal.valueOf(244), 4, null, null, 5, usersEntity.getFornecedor());

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);
        when(produtoRepository.save(produtoEntity)).thenReturn(produtoEntity);

        ProdutoResponse produtoResponseCompare = fornecedorService.buscarProduto(produtoId, errors);

        assertThat(produtoResponseCompare.getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponseCompare.getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponseCompare.getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponseCompare.getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponseCompare.getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponseCompare.getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
    }

    @Test
    public void testBuscarProdutoError() {
        Long produtoIdError = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(5L, "mari", "mari@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        String errorMessage = usersEntity.getName() + " houve um erro ao buscar produto";
        errors.put("Produto", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> fornecedorService.buscarProduto(produtoIdError, errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    public void testAtualizarProdutoSucesso() {
        Long produtoId = 9L;
        UsersEntity usersEntity = Fixture.createUsersEntity(24L, "Julia", "Julia@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoRequest produtoRequest = Fixture.createProdutoRequest("extintor", "produto de extintor", BigDecimal.valueOf(34.42), 5);
        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(2L, produtoRequest.getNome(), produtoRequest.getDescricao(), produtoRequest.getPreco(), produtoRequest.getEstoque(), null, null, 4, usersEntity.getFornecedor());
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);

        ProdutoResponse produtoResponseCompare = fornecedorService.atualizarProduto(produtoId, produtoRequest, errors);

        assertThat(produtoResponseCompare.getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponseCompare.getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponseCompare.getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponseCompare.getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponseCompare.getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponseCompare.getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
    }

    @Test
    public void testAtualizarProdutoError() {
        Long produtoIdError = 9L;
        UsersEntity usersEntity = Fixture.createUsersEntity(24L, "Julia", "Julia@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoRequest produtoRequest = Fixture.createProdutoRequest("extintor", "produto de extintor", BigDecimal.valueOf(34.42), 5);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar atualizar produto";
        errors.put("Produto", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> fornecedorService.atualizarProduto(produtoIdError, produtoRequest, errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
    }

    @Test
    public void testAdicionarProdutoACategoriaSucesso() {
        Long categoriaId = 5L;
        Long produtoId = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(23L, "maria", "maria@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(1L, "brinquedos", "categoria de brinquedos", new ArrayList<>());

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "urso de pelucia", "produto de urso de pelucia", BigDecimal.valueOf(44), 3, new ArrayList<>(), null, 2, usersEntity.getFornecedor());
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), produtoEntity.getCategorias().size());

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaEntity));
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));
        when(categoriaRepository.save(any(CategoriaEntity.class))).thenReturn(categoriaEntity);
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);

        ProdutoResponse produtoResponseCompare = fornecedorService.adicionarProdutoACategoria(categoriaId, produtoId, errors);

        assertThat(produtoResponseCompare.getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponseCompare.getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponseCompare.getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponseCompare.getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponseCompare.getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponseCompare.getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
    }

    @Test
    public void testAdicionarProdutoACategoriaError() {
        Long categoriaIdError = 55L;
        Long produtoIdError = 6L;
        UsersEntity usersEntity = Fixture.createUsersEntity(23L, "maria", "maria@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar adicionar produto na categoria";
        errors.put("Categoria", "Item não encontrado");
        errors.put("Produto", "Item não encontrado");

        doThrow(new ProdutoCategoriaException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoCategoriaException.class, errors);

        ProdutoCategoriaException produtoCategoriaException = assertThrows(ProdutoCategoriaException.class, () -> fornecedorService.adicionarProdutoACategoria(categoriaIdError, produtoIdError, errors));

        assertThat(produtoCategoriaException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoCategoriaException.getFields().get("Categoria")).isEqualTo(errors.get("Categoria"));
        assertThat(produtoCategoriaException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
    }

    @Test
    public void testAtualizarEstoqueProdutoSucesso() {
        Long produtoId = 3L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "john", "john@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest = Fixture.createProdutoNovoEstoqueRequest(5);
        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "celular", "produto de celular", BigDecimal.valueOf(12.4), produtoNovoEstoqueRequest.getNovaQuantidade(), null, null, 4, usersEntity.getFornecedor());
        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);

        ProdutoResponse produtoResponseCompare = fornecedorService.atualizarEstoqueProduto(produtoId, produtoNovoEstoqueRequest, errors);

        assertThat(produtoResponseCompare.getProdutoId()).isEqualTo(produtoResponse.getProdutoId());
        assertThat(produtoResponseCompare.getNome()).isEqualTo(produtoResponse.getNome());
        assertThat(produtoResponseCompare.getDescricao()).isEqualTo(produtoResponse.getDescricao());
        assertThat(produtoResponseCompare.getPreco()).isEqualTo(produtoResponse.getPreco());
        assertThat(produtoResponseCompare.getEstoque()).isEqualTo(produtoResponse.getEstoque());
        assertThat(produtoResponseCompare.getQuantidadeDemanda()).isEqualTo(produtoResponse.getQuantidadeDemanda());
        assertThat(produtoResponseCompare.getCategoriasAssociadas()).isEqualTo(produtoResponse.getCategoriasAssociadas());
    }

    @Test
    public void testAtualizarEstoqueProdutoError() {
        Long produtoIdError = 3L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "john", "john@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        String errorMessage = usersEntity.getName() + " houve um erro ao atualizar estoque do produto";

        errors.put("Produto", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> fornecedorService.atualizarEstoqueProduto(produtoIdError, any(ProdutoNovoEstoqueRequest.class), errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
    }

    @Test
    public void testRemoverProdutoSucesso() {
        Long produtoId = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "marco", "marco@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(1L,"eletronicos", "categoria para produtos eletronicos", null);
        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(produtoId,"tablet", "produto de tablet", BigDecimal.valueOf(1234), 4, null, null, 4, usersEntity.getFornecedor());

        String messageSucesso = produtoEntity.getNome() + " foi removido com sucesso, " + usersEntity.getName();;

        produtoEntity.setCategorias(new ArrayList<>(List.of(categoriaEntity)));
        categoriaEntity.setProdutos(new ArrayList<>(List.of(produtoEntity)));

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));

        String messageSucessoCompare = fornecedorService.removerProduto(produtoId, errors);

        assertThat(messageSucesso).isEqualTo(messageSucessoCompare);
    }

    @Test
    public void testRemoverProdutoError() {
        Long produtoIdError = 4L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "marco", "marco@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar remover produto";
        errors.put("Produto", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ProdutoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoNotFoundException.class, errors);

        ProdutoNotFoundException produtoNotFoundException = assertThrows(ProdutoNotFoundException.class, () -> fornecedorService.removerProduto(produtoIdError, errors));

        assertThat(produtoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoNotFoundException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
    }

    @Test
    public void testRemoverProdutoDeCategoriaSucesso() {
        Long produtoId = 56L;
        Long categoriaId = 4L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "michele", "michele@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        CategoriaEntity categoriaEntity = Fixture.createCategoriaEntity(1L,"moveis", "categoria para produtos moveis", null);
        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(2L,"cama", "produto de cama", BigDecimal.valueOf(1000.23), 1000, null, null, 98, usersEntity.getFornecedor());

        String messageSucesso = produtoEntity.getNome() + " foi removido com sucesso da categoria" + categoriaEntity.getNome();

        produtoEntity.setCategorias(new ArrayList<>(List.of(categoriaEntity)));
        categoriaEntity.setProdutos(new ArrayList<>(List.of(produtoEntity)));


        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId())).thenReturn(Optional.of(produtoEntity));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaEntity));

        String messageSucessoCompare = fornecedorService.removerProdutoDeCategoria(categoriaId, produtoId, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucessoCompare);
    }

    @Test
    public void testRemoverProdutoDeCategoriaError() {
        Long produtoIdError = 56L;
        Long categoriaIdError = 4L;
        UsersEntity usersEntity = Fixture.createUsersEntity(15L, "michele", "michele@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, new FornecedorEntity(), null);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar remover produto de categoria";
        errors.put("Produto", "Item não encontrado");
        errors.put("Categoria", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ProdutoCategoriaException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoCategoriaException.class, errors);

        ProdutoCategoriaException produtoCategoriaException = assertThrows(ProdutoCategoriaException.class, () -> fornecedorService.removerProdutoDeCategoria(categoriaIdError, produtoIdError, errors));

        assertThat(produtoCategoriaException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoCategoriaException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
        assertThat(produtoCategoriaException.getFields().get("Categoria")).isEqualTo(errors.get("Categoria"));
    }

    @Test
    public void testEnviarProdutoSucesso() {
        Long produtoId = 4L;
        UsersEntity usersEntity = Fixture.createUsersEntity(21L, "leonardo", "leonardo@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, null, null);

        FornecedorEntity fornecedorEntity = Fixture.createFornecedorEntity(usersEntity.getId(), "000.000/0000.0000", null, BigDecimal.valueOf(14), 2);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "tenis", "produto de tenis", BigDecimal.valueOf(22), 23, null, null, 5, null);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, 1, produtoEntity.getPreco(), null, null, StatusItemPedido.PROCESSANDO);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(2L, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.PAGO, null, null);


        produtoEntity.setItensPedidos(itemPedidoEntityList);
        usersEntity.setFornecedor(fornecedorEntity);
        fornecedorEntity.setUsers(usersEntity);
        fornecedorEntity.setProduto(new ArrayList<>(List.of(produtoEntity)));
        produtoEntity.setFornecedor(fornecedorEntity);
        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        itemPedidoEntity.setPedido(pedidoEntity);

        String messageSucesso = produtoEntity.getNome() + " foi enviado com sucesso para os clientes";

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoEntity));

        String messageSucessoCompare = fornecedorService.enviarProduto(produtoId, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @Test
    public void testEnviarProdutoError() {
        Long produtoIdError = 4L;
        UsersEntity usersEntity = Fixture.createUsersEntity(21L, "leonardo", "leonardo@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, null, null);

        FornecedorEntity fornecedorEntity = Fixture.createFornecedorEntity(usersEntity.getId(), "000.000/0000.0000", null, BigDecimal.valueOf(14), 2);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "tenis", "produto de tenis", BigDecimal.valueOf(22), 23, null, null, 5, null);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, 1, produtoEntity.getPreco(), null, null, StatusItemPedido.PROCESSANDO);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(2L, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.PAGO, null, null);


        produtoEntity.setItensPedidos(itemPedidoEntityList);
        usersEntity.setFornecedor(fornecedorEntity);
        fornecedorEntity.setUsers(usersEntity);
        fornecedorEntity.setProduto(new ArrayList<>(List.of(produtoEntity)));
        produtoEntity.setFornecedor(fornecedorEntity);
        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        itemPedidoEntity.setPedido(pedidoEntity);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar enviar produto";

        errors.put("Produto", "Item não encontrado");
        errors.put("Demanda", "Não existe nenhuma demanda para esse produto");
        errors.put("Pedido", "Não é possível enviar o produto porque o pedido ainda nao foi pago");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findById(produtoIdError)).thenReturn(Optional.of(produtoEntity));

        doThrow(new EnviarProdutoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, EnviarProdutoException.class, errors);

        EnviarProdutoException enviarProdutoException = assertThrows(EnviarProdutoException.class, () -> fornecedorService.enviarProduto(produtoIdError, errors));

        assertThat(enviarProdutoException.getMessage()).isEqualTo(errorMessage);
        assertThat(enviarProdutoException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
        assertThat(enviarProdutoException.getFields().get("Demanda")).isEqualTo(errors.get("Demanda"));
        assertThat(enviarProdutoException.getFields().get("Pedido")).isEqualTo(errors.get("Pedido"));
    }

    @Test
    public void testListarDemandaProdutoSucesso() {
        Long produtoId = 42L;
        UsersEntity usersEntity = Fixture.createUsersEntity(21L, "murilo", "murilo@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, null, null);

        FornecedorEntity fornecedorEntity = Fixture.createFornecedorEntity(usersEntity.getId(), "000.000/0000.0000", null, BigDecimal.valueOf(14), 2);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "garrafa", "produto de garrafa", BigDecimal.valueOf(56), 2, null, null, 10, null);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(15L, 13, produtoEntity.getPreco(), null, null, StatusItemPedido.PROCESSANDO);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(2L, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.PAGO, null, null);

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoEntity.getId(), produtoEntity.getId(), produtoEntity.getNome(), itemPedidoEntity.getQuantidade(), itemPedidoEntity.getValorTotal(), itemPedidoEntity.getStatusItemPedido());
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoEntity));
        when(itemPedidoMapper.toItemPedidoResponse(itemPedidoEntity)).thenReturn(itemPedidoResponse);

        produtoEntity.setItensPedidos(itemPedidoEntityList);
        usersEntity.setFornecedor(fornecedorEntity);
        fornecedorEntity.setUsers(usersEntity);
        fornecedorEntity.setProduto(new ArrayList<>(List.of(produtoEntity)));
        produtoEntity.setFornecedor(fornecedorEntity);
        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        itemPedidoEntity.setPedido(pedidoEntity);

        List<ItemPedidoResponse> itemPedidoResponseListCompare = fornecedorService.listarDemandaProduto(produtoId, errors);

        assertThat(itemPedidoResponseListCompare.getFirst().getItemPedidoId()).isEqualTo(itemPedidoResponseList.getFirst().getItemPedidoId());
        assertThat(itemPedidoResponseListCompare.getFirst().getProdutoId()).isEqualTo(itemPedidoResponseList.getFirst().getProdutoId());
        assertThat(itemPedidoResponseListCompare.getFirst().getProduto()).isEqualTo(itemPedidoResponseList.getFirst().getProduto());
        assertThat(itemPedidoResponseListCompare.getFirst().getQuantidade()).isEqualTo(itemPedidoResponseList.getFirst().getQuantidade());
        assertThat(itemPedidoResponseListCompare.getFirst().getValorTotal()).isEqualTo(itemPedidoResponseList.getFirst().getValorTotal());
        assertThat(itemPedidoResponseListCompare.getFirst().getStatusItemPedido()).isEqualTo(itemPedidoResponseList.getFirst().getStatusItemPedido());
    }

    @Test
    public void testListarDemandaProdutoError() {
        Long produtoIdError = -43L;

        String errorMessage = "Houve um erro ao tentar listar demanda de produto";
        errors.put("Produto", "Item não encontrado");
        errors.put("Demanda", "Não existe nenhuma demanda para esse produto");

        doThrow(new ProdutoDemandaException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ProdutoDemandaException.class, errors);

        ProdutoDemandaException produtoDemandaException = assertThrows(ProdutoDemandaException.class, () -> fornecedorService.listarDemandaProduto(produtoIdError, errors));

        assertThat(produtoDemandaException.getMessage()).isEqualTo(errorMessage);
        assertThat(produtoDemandaException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
        assertThat(produtoDemandaException.getFields().get("Demanda")).isEqualTo(errors.get("Demanda"));
    }

    @Test
    public void testExibirSaldoSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(21L, "jonas", "jonas@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, null, null);

        FornecedorEntity fornecedorEntity = Fixture.createFornecedorEntity(usersEntity.getId(), "000.000/000.000", null, BigDecimal.valueOf(134), 4);

        FornecedorSaldoResponse fornecedorSaldoResponse = Fixture.createFornecedorSaldoResponse(fornecedorEntity.getSaldo());

        usersEntity.setFornecedor(fornecedorEntity);
        fornecedorEntity.setUsers(usersEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(fornecedorMapper.fornecedorEntityToFornecedorSaldoResponse(fornecedorEntity)).thenReturn(fornecedorSaldoResponse);

        FornecedorSaldoResponse fornecedorSaldoResponseCompare = fornecedorService.exibirSaldoAtual();

        assertThat(fornecedorSaldoResponseCompare.getSaldo()).isEqualTo(fornecedorSaldoResponse.getSaldo());
    }

    @Test
    public void testListarProdutosComDemandaSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(21L, "jonas", "jonas@gmail.com", "+0000000-0000", "senha", Role.FORNECEDOR, null, null, null, null);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "garrafa", "produto de garrafa", BigDecimal.valueOf(56), 2, null, null, 10, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        ProdutoResponse produtoResponse = Fixture.createProdutoResponse(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getDescricao(), produtoEntity.getPreco(), produtoEntity.getEstoque(), produtoEntity.getQuantidadeDemanda(), null);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<ProdutoEntity> produtoEntityPage = new PageImpl<>(produtoEntityList, pageable, produtoEntityList.size());
        Page<ProdutoResponse> produtoResponsePage = new PageImpl<>(produtoResponseList, pageable, produtoResponseList.size());

        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(produtoResponsePage.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens(produtoResponsePage.getTotalElements());
        produtoResponsePagina.setUltimaPagina(true);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(produtoRepository.findAllByFornecedorIdAndQuantidadeDemandaGreaterThan(usersEntity.getId(), 0, pageable)).thenReturn(produtoEntityPage);
        when(produtoMapper.produtoToResponse(produtoEntity)).thenReturn(produtoResponse);
        when(paginaMapper.toPagina(produtoResponsePage)).thenReturn(produtoResponsePagina);

        Pagina<ProdutoResponse> produtoResponsePaginaCompare = fornecedorService.listarProdutosComDemanda(pageable);

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