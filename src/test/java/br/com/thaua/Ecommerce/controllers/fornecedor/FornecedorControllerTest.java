package br.com.thaua.Ecommerce.controllers.fornecedor;

import br.com.thaua.Ecommerce.controllers.ControllersFixture;
import br.com.thaua.Ecommerce.controllers.FornecedorController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorSaldoResponse;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.*;
import br.com.thaua.Ecommerce.services.FornecedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorControllerTest {
    @InjectMocks
    private FornecedorController fornecedorController;

    @Mock
    private FornecedorService fornecedorService;

    private ObjectMapper objectMapper;

    private Map<String, String> emptyMap;

    private Map<String, String> errors;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fornecedorController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
        emptyMap = ConstructorErrors.returnMapErrors();
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve atualizar CNPJ e Telefone com sucesso")
    @Test
    public void testAtualizarCNPJeTelefoneSucesso() throws Exception {
        FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest = ControllersFixture.createFornecedorCNPJTelefoneRequest("00.000.000\0000-00", "123123312");
        FornecedorResponse fornecedorResponse = ControllersFixture.createFornecedorResponse(1L, "micael", "micael@gmail.com", fornecedorCNPJTelefoneRequest.getTelefone() , fornecedorCNPJTelefoneRequest.getCnpj());

        String fornecedorCNPJTelefoneRequestJson = objectMapper.writeValueAsString(fornecedorCNPJTelefoneRequest);

        when(fornecedorService.atualizarCNPJeTelefone(eq(fornecedorCNPJTelefoneRequest))).thenReturn(fornecedorResponse);

        mockMvc.perform(patch("/api/v1/fornecedores/cnpj-telefone/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fornecedorCNPJTelefoneRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fornecedorResponse.getId()))
                .andExpect(jsonPath("$.name").value(fornecedorResponse.getName()))
                .andExpect(jsonPath("$.email").value(fornecedorResponse.getEmail()))
                .andExpect(jsonPath("$.telefone").value(fornecedorResponse.getTelefone()))
                .andExpect(jsonPath("$.cnpj").value(fornecedorResponse.getCnpj()));

        verify(fornecedorService, times(1)).atualizarCNPJeTelefone(eq(fornecedorCNPJTelefoneRequest));
    }


    @DisplayName("Deve retornar ConstraintViolationException ao atualizar com CNPJ inválido")
    @Test
    public void testAtualizarCNPJeTelefoneError() throws Exception {
        String errorMessage = ControllersFixture.createErrorMessage("Erro de validação");
        errors = ConstructorErrors.returnMapErrors();
        errors.put("Erro de validação: ", "CNPJ invalido");
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("CNPJ invalido", Set.of(ConstraintViolationImpl.forBeanValidation(null, null, null, "CNPJ invalido", null, null, null, null, null, null, null)));

        FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest = ControllersFixture.createFornecedorCNPJTelefoneRequest("CPNJ invalido", "123123312");

        String fornecedorCNPJTelefoneRequestJson = objectMapper.writeValueAsString(fornecedorCNPJTelefoneRequest);

        when(fornecedorService.atualizarCNPJeTelefone(eq(fornecedorCNPJTelefoneRequest))).thenThrow(constraintViolationException);

        mockMvc.perform(patch("/api/v1/fornecedores/cnpj-telefone/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fornecedorCNPJTelefoneRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Erro de validação: ']").value(errors.get("Erro de validação: ")));

        verify(fornecedorService, times(1)).atualizarCNPJeTelefone(eq(fornecedorCNPJTelefoneRequest));
    }

    @DisplayName("Deve cadastrar produto(s) com sucesso")
    @Test
    public void testCadastrarProdutoSucesso() throws Exception {
        ProdutoRequest produtoRequest = ControllersFixture.createProdutoRequest("espelho", "produto para espelho", BigDecimal.valueOf(500), 4);
        List<ProdutoRequest> produtoRequestList = List.of(produtoRequest);

        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(2L, produtoRequest.getNome(), produtoRequest.getDescricao(), produtoRequest.getPreco(), produtoRequest.getEstoque(), 5, 2);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        String produtoRequestListJson = objectMapper.writeValueAsString(produtoRequestList);

        when(fornecedorService.cadastrarProduto(eq(produtoRequestList), eq(emptyMap))).thenReturn(produtoResponseList);

        mockMvc.perform(post("/api/v1/fornecedores/produtos/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoRequestListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$[0].nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$[0].descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$[0].preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$[0].estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$[0].quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$[0].categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).cadastrarProduto(eq(produtoRequestList), eq(emptyMap));
    }

    @DisplayName("Deve retornar CadastrarProdutoException ao tentar cadastrar produto com dados de fornecedor incompletos")
    @Test
    public void testCadastrarProdutoError() throws Exception {
        String errorMessage = ControllersFixture.createErrorMessage("usuario, houve um erro ao tentar cadastrar o produto");
        errors = ConstructorErrors.returnMapErrors();
        errors.put("CNPJ", "CNPJ está não cadastrado");
        errors.put("Telefone", "Telefone não cadastrado");
        errors.put("Endereço", "Endereço não cadastrado");
        CadastrarProdutoException cadastrarProdutoException = new CadastrarProdutoException(errorMessage, errors);

        ProdutoRequest produtoRequest = ControllersFixture.createProdutoRequest("patins", "produto para patins", BigDecimal.valueOf(50), 2);
        List<ProdutoRequest> produtoRequestList = List.of(produtoRequest);

        String produtoRequestListJson = objectMapper.writeValueAsString(produtoRequestList);

        when(fornecedorService.cadastrarProduto(eq(produtoRequestList), eq(emptyMap))).thenThrow(cadastrarProdutoException);

        mockMvc.perform(post("/api/v1/fornecedores/produtos/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoRequestListJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['CNPJ']").value(errors.get("CNPJ")))
                .andExpect(jsonPath("$.fieldsErrors['Telefone']").value(errors.get("Telefone")))
                .andExpect(jsonPath("$.fieldsErrors['Endereço']").value(errors.get("Endereço")));

        verify(fornecedorService, times(1)).cadastrarProduto(eq(produtoRequestList), eq(emptyMap));
    }

    @DisplayName("Deve exibir página de produtos do fornecedor com sucesso")
    @Test
    public void testExibirProdutosSucesso() throws Exception {
        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(10L, "garrafa", "produto de garrafa", BigDecimal.valueOf(23), 12, 2, 4);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page page = new PageImpl(produtoResponseList, pageable, produtoResponseList.size());

        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(page.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens(page.getTotalElements());
        produtoResponsePagina.setUltimaPagina(true);

        when(fornecedorService.exibirProdutos(eq(pageable))).thenReturn(produtoResponsePagina);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/list")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.conteudo[0].nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.conteudo[0].descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.conteudo[0].preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.conteudo[0].quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.conteudo[0].categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).exibirProdutos(eq(pageable));
    }

    @DisplayName("Deve buscar produto específico do fornecedor com sucesso")
    @Test
    public void testBuscarProdutoSucesso() throws Exception {
        Long produtoId = 2L;
        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(produtoId, "mesa digitalizadora", "produto de mesa digitalizadora", BigDecimal.valueOf(190), 120, 56, 4);

        when(fornecedorService.buscarProduto(eq(produtoId), eq(emptyMap))).thenReturn(produtoResponse);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/{produtoId}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).buscarProduto(eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao buscar produto inexistente ou não pertencente ao fornecedor")
    @Test
    public void testBuscarProdutoError() throws Exception {
        String errorMessage =  "usuario houve um erro ao buscar produto";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        Long produtoIdError = -23L;

        when(fornecedorService.buscarProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/{produtoId}", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(fornecedorService, times(1)).buscarProduto(eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve atualizar produto do fornecedor com sucesso")
    @Test
    public void testAtualizarProdutoSucesso() throws Exception {
        Long produtoId = 1L;
        ProdutoRequest produtoRequest = ControllersFixture.createProdutoRequest("meias", "produto para meias", BigDecimal.valueOf(20), 100);

        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(produtoId, produtoRequest.getNome(), produtoRequest.getDescricao(), produtoRequest.getPreco(), produtoRequest.getEstoque(), 5, 2);

        String produtoRequestJson = objectMapper.writeValueAsString(produtoRequest);

        when(fornecedorService.atualizarProduto(eq(produtoId), eq(produtoRequest), eq(emptyMap))).thenReturn(produtoResponse);

        mockMvc.perform(put("/api/v1/fornecedores/produtos/{produtoId}/update", produtoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).atualizarProduto(eq(produtoId), eq(produtoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao tentar atualizar produto inexistente ou não pertencente ao fornecedor")
    @Test
    public void testAtualizarProdutoError() throws Exception {
        String errorMessage =  "usuario houve um erro ao tentar atualizar produto";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        Long produtoIdError = -12L;
        ProdutoRequest produtoRequest = ControllersFixture.createProdutoRequest("ventilador", "produto para ventilador", BigDecimal.valueOf(115), 12);

        String produtoRequestJson = objectMapper.writeValueAsString(produtoRequest);

        when(fornecedorService.atualizarProduto(eq(produtoIdError
        ), eq(produtoRequest), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(put("/api/v1/fornecedores/produtos/{produtoId}/update", produtoIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors").isMap());

        verify(fornecedorService, times(1)).atualizarProduto(eq(produtoIdError), eq(produtoRequest), eq(emptyMap));
    }

    @DisplayName("Deve adicionar produto a uma categoria com sucesso")
    @Test
    public void testAdicionarProdutoACategoriaSucesso() throws Exception {
        Long produtoId = 4L;
        Long categoriaId = 2L;

        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(produtoId, "pijama", "produto para pijama", BigDecimal.valueOf(34), 23, 5, 2);

        when(fornecedorService.adicionarProdutoACategoria(eq(categoriaId), eq(produtoId), eq(emptyMap))).thenReturn(produtoResponse);

        mockMvc.perform(post("/api/v1/fornecedores/categorias/{categoriaId}/produtos/{produtoId}", categoriaId, produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).adicionarProdutoACategoria(eq(categoriaId), eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoCategoriaException ao tentar adicionar produto/categoria inexistente ou não pertencente ao fornecedor")
    @Test
    public void testAdicionarProdutoACategoriaError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar adicionar produto na categoria";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoCategoriaException produtoCategoriaException = new ProdutoCategoriaException(errorMessage, errors);

        Long produtoIdError = 133L;
        Long categoriaIdError = -3L;

        when(fornecedorService.adicionarProdutoACategoria(eq(categoriaIdError), eq(produtoIdError), eq(emptyMap))).thenThrow(produtoCategoriaException);

        mockMvc.perform(post("/api/v1/fornecedores/categorias/{categoriaId}/produtos/{produtoId}", categoriaIdError, produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(fornecedorService, times(1)).adicionarProdutoACategoria(eq(categoriaIdError), eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve atualizar estoque de produto do fornecedor com sucesso")
    @Test
    public void testAtualizarEstoqueProdutoSucesso() throws Exception {
        Long produtoId = 2L;
        ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest = ControllersFixture.createProdutoNovoEstoqueRequest(5);

        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(produtoId, "vestido", "produto de vestido", BigDecimal.valueOf(60.25), produtoNovoEstoqueRequest.getNovaQuantidade(), 1, 5);

        String produtoNovoEstoqueRequestJson = objectMapper.writeValueAsString(produtoNovoEstoqueRequest);

        when(fornecedorService.atualizarEstoqueProduto(eq(produtoId), eq(produtoNovoEstoqueRequest), eq(emptyMap))).thenReturn(produtoResponse);

        mockMvc.perform(patch("/api/v1/fornecedores/produtos/{produtoId}/estoque/update", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoNovoEstoqueRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.estoque").value(produtoResponse.getEstoque()))
                .andExpect(jsonPath("$.quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).atualizarEstoqueProduto(eq(produtoId), eq(produtoNovoEstoqueRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao tentar atualizar estoque de produto inexistente ou não pertencente ao fornecedor")
    @Test
    public void testAtualizarEstoqueProdutoError() throws Exception {
        String errorMessage = "usuario houve um erro ao atualizar estoque do produto";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        Long produtoIdError = 4L;
        ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest = ControllersFixture.createProdutoNovoEstoqueRequest(10);

        String produtoNovoEstoqueRequestJson = objectMapper.writeValueAsString(produtoNovoEstoqueRequest);

        when(fornecedorService.atualizarEstoqueProduto(eq(produtoIdError), eq(produtoNovoEstoqueRequest), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(patch("/api/v1/fornecedores/produtos/{produtoId}/estoque/update", produtoIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoNovoEstoqueRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors").isMap());

        verify(fornecedorService, times(1)).atualizarEstoqueProduto(eq(produtoIdError), eq(produtoNovoEstoqueRequest), eq(emptyMap));
    }

    @DisplayName("Deve remover produto do fornecedor com sucesso")
    @Test
    public void testRemoverProdutoSucesso() throws Exception {
        Long produtoId = 5L;
        String messageSucesso = "produto removido com sucesso";

        when(fornecedorService.removerProduto(eq(produtoId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/fornecedores/produtos/{produtoId}/delete", produtoId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(fornecedorService, times(1)).removerProduto(eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoNotFoundException ao tentar remover produto inexistente ou não pertencente ao fornecedor")
    @Test
    public void testRemoverProdutoError() throws Exception {
        String errorMessage = "usuario foi removido com sucesso da categoria";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoNotFoundException produtoNotFoundException = new ProdutoNotFoundException(errorMessage, errors);

        Long produtoIdError = 3L;

        when(fornecedorService.removerProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(produtoNotFoundException);

        mockMvc.perform(delete("/api/v1/fornecedores/produtos/{produtoId}/delete", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(fornecedorService, times(1)).removerProduto(eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve remover produto de uma categoria com sucesso")
    @Test
    public void testRemoverProdutoDeCategoriaSucesso() throws Exception {
        Long produtoId = 6L;
        Long categoriaId = 2L;
        String messageSucesso = "produto foi removido com sucesso da categoria";

        when(fornecedorService.removerProdutoDeCategoria(eq(categoriaId), eq(produtoId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/fornecedores/categorias/{categoriaId}/produtos/{produtoId}/delete", categoriaId, produtoId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(fornecedorService, times(1)).removerProdutoDeCategoria(eq(categoriaId), eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoCategoriaException ao tentar remover produto/categoria inexistente ou não pertencente ao fornecedor")
    @Test
    public void testRemoverProdutoDeCategoriaError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar remover produto da categoria";
        errors.put("Falha de busca", "Item não encontrado");
        ProdutoCategoriaException produtoCategoriaException = new ProdutoCategoriaException(errorMessage, errors);

        Long produtoIdError = -2L;
        Long categoriaIdError = 9999L;

        when(fornecedorService.removerProdutoDeCategoria(eq(categoriaIdError), eq(produtoIdError), eq(emptyMap))).thenThrow(produtoCategoriaException);

        mockMvc.perform(delete("/api/v1/fornecedores/categorias/{categoriaId}/produtos/{produtoId}/delete", categoriaIdError, produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors").isMap());

        verify(fornecedorService, times(1)).removerProdutoDeCategoria(eq(categoriaIdError), eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve marcar produto como enviado com sucesso")
    @Test
    public void testEnviarProdutoSucesso() throws Exception {
        Long produtoId = 10L;
        String messageSucesso = "produto foi enviado com sucesso para os clientes";

        when(fornecedorService.enviarProduto(eq(produtoId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(patch("/api/v1/fornecedores/produtos/{produtoId}/enviar", produtoId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(fornecedorService, times(1)).enviarProduto(eq(produtoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar EnviarProdutoException ao tentar enviar produto com erros")
    @Test
    public void testEnviarProdutoError() throws Exception {
        String errorMessage =  "usuario houve um erro ao tentar enviar produto";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Demanda", "Não existe nenhuma demanda para esse produto");
        errors.put("Pedido", "Não é possível enviar o produto porque o pedido ainda nao foi pago");
        EnviarProdutoException enviarProdutoException = new EnviarProdutoException(errorMessage, errors);

        Long produtoIdError = -5L;

        when(fornecedorService.enviarProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(enviarProdutoException);

        mockMvc.perform(patch("/api/v1/fornecedores/produtos/{produtoId}/enviar", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Demanda']").value(errors.get("Demanda")))
                .andExpect(jsonPath("$.fieldsErrors['Pedido']").value(errors.get("Pedido")));

        verify(fornecedorService, times(1)).enviarProduto(eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve listar a demanda de um produto específico com sucesso")
    @Test
    public void testListarDemandaProdutoSucesso() throws Exception {
        Long itemPedidoId = 2L;
        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(itemPedidoId, 2L, "esmalte", 2, BigDecimal.valueOf(25), StatusItemPedido.PENDENTE);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        when(fornecedorService.listarDemandaProduto(eq(itemPedidoId), eq(emptyMap))).thenReturn(itemPedidoResponseList);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/{produtoId}/demanda/list", itemPedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(fornecedorService, times(1)).listarDemandaProduto(eq(itemPedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ProdutoDemandaException ao listar demanda de produto inexistente ou sem demanda")
    @Test
    public void testListarDemandaProdutoError() throws Exception {
        String errorMessage = "Houve um erro ao tentar listar demanda de produto";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Demanda", "Não existe nenhuma demanda para esse produto");
        ProdutoDemandaException produtoDemandaException = new ProdutoDemandaException(errorMessage, errors);

        Long produtoIdError = 1012L;

        when(fornecedorService.listarDemandaProduto(eq(produtoIdError), eq(emptyMap))).thenThrow(produtoDemandaException);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/{produtoId}/demanda/list", produtoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Demanda']").value(errors.get("Demanda")));

        verify(fornecedorService, times(1)).listarDemandaProduto(eq(produtoIdError), eq(emptyMap));
    }

    @DisplayName("Deve exibir o saldo atual do fornecedor com sucesso")
    @Test
    public void testExibirSaldoSucesso() throws Exception {
        FornecedorSaldoResponse fornecedorSaldoResponse = ControllersFixture.createFornecedorSaldoResponse(BigDecimal.valueOf(123));

        when(fornecedorService.exibirSaldoAtual()).thenReturn(fornecedorSaldoResponse);

        mockMvc.perform(get("/api/v1/fornecedores/saldo/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(fornecedorSaldoResponse.getSaldo()));

        verify(fornecedorService, times(1)).exibirSaldoAtual();
    }

    @DisplayName("Deve listar página de produtos com demanda com sucesso")
    @Test
    public void testListarProdutosComDemandaSucesso() throws Exception {
        ProdutoResponse produtoResponse = ControllersFixture.createProdutoResponse(10L, "garrafa", "produto de garrafa", BigDecimal.valueOf(23), 12, 2, 4);
        List<ProdutoResponse> produtoResponseList = List.of(produtoResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<ProdutoResponse> page = new PageImpl(produtoResponseList, pageable, produtoResponseList.size());

        Pagina<ProdutoResponse> produtoResponsePagina = new Pagina<>();
        produtoResponsePagina.setConteudo(produtoResponseList);
        produtoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        produtoResponsePagina.setTotalPaginas(page.getTotalPages());
        produtoResponsePagina.setItensPorPagina(pageable.getPageSize());
        produtoResponsePagina.setTotalItens(page.getTotalElements());
        produtoResponsePagina.setUltimaPagina(true);

        when(fornecedorService.listarProdutosComDemanda(eq(pageable))).thenReturn(produtoResponsePagina);

        mockMvc.perform(get("/api/v1/fornecedores/produtos/demanda/list")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].produtoId").value(produtoResponse.getProdutoId()))
                .andExpect(jsonPath("$.conteudo[0].nome").value(produtoResponse.getNome()))
                .andExpect(jsonPath("$.conteudo[0].descricao").value(produtoResponse.getDescricao()))
                .andExpect(jsonPath("$.conteudo[0].preco").value(produtoResponse.getPreco()))
                .andExpect(jsonPath("$.conteudo[0].quantidadeDemanda").value(produtoResponse.getQuantidadeDemanda()))
                .andExpect(jsonPath("$.conteudo[0].categoriasAssociadas").value(produtoResponse.getCategoriasAssociadas()));

        verify(fornecedorService, times(1)).listarProdutosComDemanda(eq(pageable));
    }
}
