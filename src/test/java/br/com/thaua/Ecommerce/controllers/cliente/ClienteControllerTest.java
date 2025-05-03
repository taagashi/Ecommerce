package br.com.thaua.Ecommerce.controllers.cliente;

import br.com.thaua.Ecommerce.controllers.ClienteController;
import br.com.thaua.Ecommerce.controllers.ControllersFixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.exceptions.*;
import br.com.thaua.Ecommerce.services.ClienteService;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {
    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Map<String, String> errors;

    private Map<String, String> emptyMap;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
        emptyMap = ConstructorErrors.returnMapErrors();
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso um cliente com cpf e telefone cadastrados")
    @Test
    public void testAtualizarCpfETelefoneSucesso() throws Exception {
        ClienteCpfTelefoneRequest clienteCpfTelefoneRequest = ControllersFixture.createClienteCpfTelefoneRequest("000.000.000-00", "111222333");
        ClienteResponse clienteResponse = ControllersFixture.createClienteResponse(1L, "taagashi", "taagashi.dev@gmail.com", clienteCpfTelefoneRequest.getTelefone(), clienteCpfTelefoneRequest.getCpf());

        String clienteCpfTelefoneRequestJson = objectMapper.writeValueAsString(clienteCpfTelefoneRequest);

        when(clienteService.atualizarCpfETelefone(eq(clienteCpfTelefoneRequest), eq(emptyMap))).thenReturn(clienteResponse);

        mockMvc.perform(patch("/api/v1/clientes/cpf-telefone/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteCpfTelefoneRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId()))
                .andExpect(jsonPath("$.name").value(clienteResponse.getName()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()))
                .andExpect(jsonPath("$.telefone").value(clienteResponse.getTelefone()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()));

        verify(clienteService, times(1)).atualizarCpfETelefone(eq(clienteCpfTelefoneRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar um ConstraintViolationException ao colocar cpf errado")
    @Test
    public void testAtualizarCpfETelefoneError() throws Exception {
        String errorMessage = ControllersFixture.createErrorMessage("Erro de validação");
        errors.put("Erro de validação: ", "Cpf invalido");
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Cpf invalido", Set.of(ConstraintViolationImpl.forBeanValidation(null, null, null, "Cpf invalido", null, null, null, null, null, null, null)));

        ClienteCpfTelefoneRequest clienteCpfTelefoneRequest = ControllersFixture.createClienteCpfTelefoneRequest("cpf invalido", "111222333");

        String clienteCpfTelefoneRequestJson = objectMapper.writeValueAsString(clienteCpfTelefoneRequest);

        when(clienteService.atualizarCpfETelefone(eq(clienteCpfTelefoneRequest), eq(emptyMap))).thenThrow(constraintViolationException);

        mockMvc.perform(patch("/api/v1/clientes/cpf-telefone/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteCpfTelefoneRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Erro de validação: ']").value(errors.get("Erro de validação: ")));

        verify(clienteService, times(1)).atualizarCpfETelefone(eq(clienteCpfTelefoneRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso um novo token ao atualizar dados do cliente")
    @Test
    public void testAtualizarDadosSucesso() throws Exception {
        ClienteUpdateRequest clienteUpdateRequest = ControllersFixture.createClienteUpdateRequest("Jonas", "Jonas@gmail.com", "849902345", "000.000.000-001");
        String jwtToken = ControllersFixture.createJwtToken();

        String clienteUpdateRequestJson = objectMapper.writeValueAsString(clienteUpdateRequest);

        when(clienteService.atualizarDados(eq(clienteUpdateRequest))).thenReturn(jwtToken);

        mockMvc.perform(put("/api/v1/clientes/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteUpdateRequestJson))
                .andExpect(content().string(jwtToken));

        verify(clienteService, times(1)).atualizarDados(eq(clienteUpdateRequest));
    }

    @DisplayName("Deve retornar com sucesso o pedido criado")
    @Test
    public void testFazerPedidoSucesso() throws Exception {
        ItemPedidoRequest itemPedidoRequest = ControllersFixture.createItemPedidoRequest(1L, 5);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);
        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(1L, itemPedidoRequest.getProdutoId(), "Camiseta", 5, BigDecimal.valueOf(20.55), StatusItemPedido.PENDENTE);
        PedidoResponse pedidoResponse = ControllersFixture.createPedidoResponse(1L, "marcos", LocalDateTime.now(), itemPedidoResponse.getValorTotal(), StatusPedido.AGUARDANDO_PAGAMENTO.toString(), List.of(itemPedidoResponse));

        String itemPedidoRequestListJson = objectMapper.writeValueAsString(itemPedidoRequestList);

        when(clienteService.fazerPedido(eq(itemPedidoRequestList), eq(emptyMap))).thenReturn(pedidoResponse);

        mockMvc.perform(post("/api/v1/clientes/pedidos/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemPedidoRequestListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(pedidoResponse.getPedidoId()))
                .andExpect(jsonPath("$.cliente").value(pedidoResponse.getCliente()))
                .andExpect(jsonPath("$.dataPedido[0]").value(pedidoResponse.getDataPedido().getYear()))
                .andExpect(jsonPath("$.dataPedido[1]").value(pedidoResponse.getDataPedido().getMonthValue()))
                .andExpect(jsonPath("$.dataPedido[2]").value(pedidoResponse.getDataPedido().getDayOfMonth()))
                .andExpect(jsonPath("$.dataPedido[3]").value(pedidoResponse.getDataPedido().getHour()))
                .andExpect(jsonPath("$.dataPedido[4]").value(pedidoResponse.getDataPedido().getMinute()))
                .andExpect(jsonPath("$.dataPedido[5]").value(pedidoResponse.getDataPedido().getSecond()))
                .andExpect(jsonPath("$.dataPedido[6]").value(pedidoResponse.getDataPedido().getNano()))
                .andExpect(jsonPath("$.valorPedido").value(pedidoResponse.getValorPedido()))
                .andExpect(jsonPath("$.statusPedido").value(pedidoResponse.getStatusPedido()))
                .andExpect(jsonPath("$.itensPedidos").isArray())
                .andExpect(jsonPath("$.itensPedidos[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.itensPedidos[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.itensPedidos[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).fazerPedido(eq(itemPedidoRequestList), eq(emptyMap));
    }

    @DisplayName("Deve retornar FazerPedidoException ao tentar fazer pedido com erros")
    @Test
    public void testFazerPedidoError() throws Exception {
        errors.put("Endereço", "Endereço não cadastrado");
        errors.put("Telefone", "Telefone não cadastrado");
        errors.put("nome do produto", "Quantidade acima do estoque ou igual a 0 para este produto");
        String errorMessage = ControllersFixture.createErrorMessage("usuario, houve um erro na hora de fazer um pedido");
        FazerPedidoException fazerPedidoException = new FazerPedidoException(errorMessage, errors);

        ItemPedidoRequest itemPedidoRequest =  ControllersFixture.createItemPedidoRequest(1L, -3133);

        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        String itemPedidoRequestListJson = objectMapper.writeValueAsString(itemPedidoRequestList);

        when(clienteService.fazerPedido(eq(itemPedidoRequestList), eq(emptyMap))).thenThrow(fazerPedidoException);

        mockMvc.perform(post("/api/v1/clientes/pedidos/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemPedidoRequestListJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Endereço']").value(errors.get("Endereço")))
                .andExpect(jsonPath("$.fieldsErrors['Telefone']").value(errors.get("Telefone")))
                .andExpect(jsonPath("$.fieldsErrors['nome do produto']").value(errors.get("nome do produto")));

        verify(clienteService, times(1)).fazerPedido(eq(itemPedidoRequestList), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso uma página de pedidos filtrados por status")
    @Test
    public void testListarPedidosSucesso() throws Exception {
        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(1L, 2L, "notebook", 12, BigDecimal.valueOf(200), StatusItemPedido.PROCESSANDO);
        ItemPedidoResponse itemPedidoResponse2 = ControllersFixture.createItemPedidoResponse(2L, 5L, "chapeu", 22, BigDecimal.valueOf(200.424), StatusItemPedido.PROCESSANDO);

        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse, itemPedidoResponse2);

        PedidoResponse pedidoResponse = ControllersFixture.createPedidoResponse(1L, "job", LocalDateTime.now(), BigDecimal.valueOf(400.424), "PAGO", itemPedidoResponseList);

        List<PedidoResponse> pedidoResponseList = List.of(pedidoResponse);

        String statusPedido = StatusPedido.PAGO.toString();

        Pageable pageable = PageRequest.of(0, 2);
        Page<PedidoResponse> page = new PageImpl<>(pedidoResponseList,pageable, pedidoResponseList.size());

        Pagina<PedidoResponse> pedidoResponsePagina = new Pagina<>();
        pedidoResponsePagina.setConteudo(pedidoResponseList);
        pedidoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        pedidoResponsePagina.setTotalPaginas(page.getTotalPages());
        pedidoResponsePagina.setItensPorPagina(pageable.getPageSize());
        pedidoResponsePagina.setTotalItens((long) pedidoResponseList.size());
        pedidoResponsePagina.setUltimaPagina(true);

        when(clienteService.listarPedidos(eq(pageable), eq(statusPedido), eq(emptyMap))).thenReturn(pedidoResponsePagina);

        mockMvc.perform(get("/api/v1/clientes/pedidos/list")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize()))
                .param("statusPedido", statusPedido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].pedidoId").value(pedidoResponse.getPedidoId()))
                .andExpect(jsonPath("$.conteudo[0].cliente").value(pedidoResponse.getCliente()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[0]").value(pedidoResponse.getDataPedido().getYear()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[1]").value(pedidoResponse.getDataPedido().getMonthValue()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[2]").value(pedidoResponse.getDataPedido().getDayOfMonth()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[3]").value(pedidoResponse.getDataPedido().getHour()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[4]").value(pedidoResponse.getDataPedido().getMinute()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[5]").value(pedidoResponse.getDataPedido().getSecond()))
                .andExpect(jsonPath("$.conteudo[0].dataPedido[6]").value(pedidoResponse.getDataPedido().getNano()))
                 .andExpect(jsonPath("$.conteudo[0].valorPedido").value(pedidoResponse.getValorPedido()))
                .andExpect(jsonPath("$.conteudo[0].statusPedido").value(pedidoResponse.getStatusPedido()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos").isArray())
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].itemPedidoId").value(itemPedidoResponse2.getItemPedidoId()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].produtoId").value(itemPedidoResponse2.getProdutoId()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].produto").value(itemPedidoResponse2.getProduto()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].quantidade").value(itemPedidoResponse2.getQuantidade()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].valorTotal").value(itemPedidoResponse2.getValorTotal()))
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[1].statusItemPedido").value(itemPedidoResponse2.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).listarPedidos(eq(pageable), eq(statusPedido), eq(emptyMap));
    }

    @DisplayName("Deve retornar InvalidStatusPedidoException ao listar pedidos com status inválido")
    @Test
    public void testListarPedidosError() throws Exception {
        errors.put("Status", "O Status digitado não existe");
        String errorMessage = "usuario houve um erro ao tentar listar pedidos";
        InvalidStatusPedidoException invalidStatusPedidoException = new InvalidStatusPedidoException(errorMessage, errors);

        String statusPedidoError = "Status que nao existe";

        Pageable pageable = PageRequest.of(0, 5);

        when(clienteService.listarPedidos(eq(pageable), eq(statusPedidoError), eq(emptyMap))).thenThrow(invalidStatusPedidoException);

        mockMvc.perform(get("/api/v1/clientes/pedidos/list")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize()))
                .param("statusPedido", statusPedidoError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")));

        verify(clienteService, times(1)).listarPedidos(eq(pageable), eq(statusPedidoError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso um pedido específico pelo ID")
    @Test
    public void testBuscarPedidoSucesso() throws Exception {
        Long pedidoId = 4L;
        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(1L, 2L, "relogio", 4, BigDecimal.valueOf(100), StatusItemPedido.PENDENTE);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        PedidoResponse pedidoResponse = ControllersFixture.createPedidoResponse(pedidoId, "Jordan", LocalDateTime.now(), BigDecimal.valueOf(100), StatusPedido.AGUARDANDO_PAGAMENTO.toString(), itemPedidoResponseList);

        when(clienteService.buscarPedido(eq(pedidoId), eq(emptyMap))).thenReturn(pedidoResponse);

        mockMvc.perform(get("/api/v1/clientes/pedidos/{pedidoId}/list", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(pedidoResponse.getPedidoId()))
                .andExpect(jsonPath("$.cliente").value(pedidoResponse.getCliente()))
                .andExpect(jsonPath("$.dataPedido[0]").value(pedidoResponse.getDataPedido().getYear()))
                .andExpect(jsonPath("$.dataPedido[1]").value(pedidoResponse.getDataPedido().getMonthValue()))
                .andExpect(jsonPath("$.dataPedido[2]").value(pedidoResponse.getDataPedido().getDayOfMonth()))
                .andExpect(jsonPath("$.dataPedido[3]").value(pedidoResponse.getDataPedido().getHour()))
                .andExpect(jsonPath("$.dataPedido[4]").value(pedidoResponse.getDataPedido().getMinute()))
                .andExpect(jsonPath("$.dataPedido[5]").value(pedidoResponse.getDataPedido().getSecond()))
                .andExpect(jsonPath("$.dataPedido[6]").value(pedidoResponse.getDataPedido().getNano()))
                .andExpect(jsonPath("$.valorPedido").value(pedidoResponse.getValorPedido()))
                .andExpect(jsonPath("$.statusPedido").value(pedidoResponse.getStatusPedido()))
                .andExpect(jsonPath("$.itensPedidos").isArray())
                .andExpect(jsonPath("$.itensPedidos[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.itensPedidos[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.itensPedidos[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).buscarPedido(eq(pedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar PedidoNotFoundException ao buscar pedido com ID inexistente")
    @Test
    public void testBuscarPedidoError() throws Exception {
        Long pedidoIdError = 10L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage =  "usuario houve um erro ao tentar buscar pedido";
        PedidoNotFoundException pedidoNotFoundException = new PedidoNotFoundException(errorMessage, errors);

        when(clienteService.buscarPedido(eq(pedidoIdError), eq(emptyMap))).thenThrow(pedidoNotFoundException);

        mockMvc.perform(get("/api/v1/clientes/pedidos/{pedidoId}/list", pedidoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(clienteService, times(1)).buscarPedido(eq(pedidoIdError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso um item de pedido específico pelo ID")
    @Test
    public void testBuscarItemPedidoSucesso() throws Exception {
        Long itemPedidoId = 4L;
        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(itemPedidoId, 2L, "chinelo", 4, BigDecimal.valueOf(15.23), StatusItemPedido.PENDENTE);

        when(clienteService.buscarItemPedido(eq(itemPedidoId), eq(emptyMap))).thenReturn(itemPedidoResponse);

        mockMvc.perform(get("/api/v1/clientes/pedidos/itensPedidos/{itemPedidoId}", itemPedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemPedidoId").value(itemPedidoId))
                .andExpect(jsonPath("$.produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).buscarItemPedido(eq(itemPedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ItemPedidoNotFoundException ao buscar item de pedido com ID inexistente")
    @Test
    public void testBuscarItemPedidoError() throws Exception {
        Long itemPedidoIdError = 200L;
        errors.put("Falha de busca", "Item não encontrado");
        String errorMessage = "usuario houve um erro ao tentar buscar item pedido";
        ItemPedidoNotFoundException itemPedidoNotFoundException = new ItemPedidoNotFoundException(errorMessage, errors);

        when(clienteService.buscarItemPedido(eq(itemPedidoIdError), eq(emptyMap))).thenThrow(itemPedidoNotFoundException);

        mockMvc.perform(get("/api/v1/clientes/pedidos/itensPedidos/{itemPedidoId}", itemPedidoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(clienteService, times(1)).buscarItemPedido(eq(itemPedidoIdError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso mensagem ao pagar pedido")
    @Test
    public void testPagarPedidoSucesso() throws Exception {
        Long pedidoId = 10L;
        BigDecimal valorPedido = BigDecimal.valueOf(250);
        String pedidoPagoSucesso = "usuario seu pedido foi pago com sucesso";

        when(clienteService.pagarPedido(eq(pedidoId), eq(valorPedido), eq(emptyMap))).thenReturn(pedidoPagoSucesso);

        mockMvc.perform(post("/api/v1/clientes/pedidos/{pedidosId}/pagar", pedidoId)
                .param("valor", valorPedido.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(pedidoPagoSucesso));

        verify(clienteService, times(1)).pagarPedido(eq(pedidoId), eq(valorPedido), eq(emptyMap));
    }

    @DisplayName("Deve retornar PagarPedidoException ao tentar pagar pedido com erros")
    @Test
    public void testPagarPedidoError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar pagar pedido";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Valor", "O valor fornecido é invalido para a compra do pedido");
        errors.put("Status", "O pedido já foi pago");
        PagarPedidoException pagarPedidoException = new PagarPedidoException(errorMessage, errors);

        Long pedidoIdError = 50L;
        BigDecimal valorPedidoError = BigDecimal.valueOf(-19);

        when(clienteService.pagarPedido(eq(pedidoIdError), eq(valorPedidoError), eq(emptyMap))).thenThrow(pagarPedidoException);

        mockMvc.perform(post("/api/v1/clientes/pedidos/{pedidoId}/pagar", pedidoIdError)
                .param("valor", valorPedidoError.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Valor']").value(errors.get("Valor")))
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")));

        verify(clienteService, times(1)).pagarPedido(eq(pedidoIdError), eq(valorPedidoError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso o pedido editado")
    @Test
    public void testEditarPedidoSucesso() throws Exception {
        Long pedidoId = 1L;
        ItemPedidoRequest itemPedidoRequest = ControllersFixture.createItemPedidoRequest(1L, 4);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(2L, itemPedidoRequest.getProdutoId(), "ventilador", itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(200), StatusItemPedido.PENDENTE);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);
        PedidoResponse pedidoResponse = ControllersFixture.createPedidoResponse(pedidoId, "Marcos", LocalDateTime.now(), BigDecimal.valueOf(200), StatusPedido.AGUARDANDO_PAGAMENTO.toString(), itemPedidoResponseList);

        String itemPedidoRequestListJson = objectMapper.writeValueAsString(itemPedidoRequestList);

        when(clienteService.editarPedido(eq(pedidoId), eq(itemPedidoRequestList), eq(emptyMap))).thenReturn(pedidoResponse);

        mockMvc.perform(put("/api/v1/clientes/pedidos/{pedidoId}/update", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemPedidoRequestListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(pedidoResponse.getPedidoId()))
                .andExpect(jsonPath("$.cliente").value(pedidoResponse.getCliente()))
                .andExpect(jsonPath("$.dataPedido[0]").value(pedidoResponse.getDataPedido().getYear()))
                .andExpect(jsonPath("$.dataPedido[1]").value(pedidoResponse.getDataPedido().getMonthValue()))
                .andExpect(jsonPath("$.dataPedido[2]").value(pedidoResponse.getDataPedido().getDayOfMonth()))
                .andExpect(jsonPath("$.dataPedido[3]").value(pedidoResponse.getDataPedido().getHour()))
                .andExpect(jsonPath("$.dataPedido[4]").value(pedidoResponse.getDataPedido().getMinute()))
                .andExpect(jsonPath("$.dataPedido[5]").value(pedidoResponse.getDataPedido().getSecond()))
                .andExpect(jsonPath("$.dataPedido[6]").value(pedidoResponse.getDataPedido().getNano()))
                .andExpect(jsonPath("$.valorPedido").value(pedidoResponse.getValorPedido()))
                .andExpect(jsonPath("$.statusPedido").value(pedidoResponse.getStatusPedido()))
                .andExpect(jsonPath("$.itensPedidos").isArray())
                .andExpect(jsonPath("$.itensPedidos[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.itensPedidos[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.itensPedidos[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).editarPedido(eq(pedidoId), eq(itemPedidoRequestList), eq(emptyMap));

    }

    @DisplayName("Deve retornar EditarPedidoException ao tentar editar pedido com erros")
    @Test
    public void testEditarPedidoError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar atualizar seu pedido";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Status", "Só é possível editar um pedido que ainda não foi pago");
        errors.put("nome do produto", "Quantidade acima do estoque ou igual a 0 para este produto");
        EditarPedidoException editarPedidoException = new EditarPedidoException(errorMessage, errors);

        Long pedidoIdError = 4L;
        ItemPedidoRequest itemPedidoRequestError = ControllersFixture.createItemPedidoRequest(1L, -1233);
        List<ItemPedidoRequest> itemPedidoRequestErrorList = List.of(itemPedidoRequestError);

        String itemPedidoRequestErrorJson = objectMapper.writeValueAsString(itemPedidoRequestErrorList);


        when(clienteService.editarPedido(eq(pedidoIdError), eq(itemPedidoRequestErrorList), eq(emptyMap))).thenThrow(editarPedidoException);

        mockMvc.perform(put("/api/v1/clientes/pedidos/{pedidoId}/update", pedidoIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemPedidoRequestErrorJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")))
                .andExpect(jsonPath("$.fieldsErrors['nome do produto']").value(errors.get("nome do produto")));

        verify(clienteService, times(1)).editarPedido(eq(pedidoIdError), eq(itemPedidoRequestErrorList), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso o pedido com o novo produto adicionado")
    @Test
    public void testAdicionarProdutoAPedidoSucesso() throws Exception {
        Long pedidoId = 1L;
        ItemPedidoRequest itemPedidoRequest = ControllersFixture.createItemPedidoRequest(4L, 5);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoResponse itemPedidoResponse = ControllersFixture.createItemPedidoResponse(3L, itemPedidoRequest.getProdutoId(), "ferro de passar", itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(405), StatusItemPedido.PROCESSANDO);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);
        PedidoResponse pedidoResponse = ControllersFixture.createPedidoResponse(pedidoId, "antonio", LocalDateTime.now(), BigDecimal.valueOf(200), StatusPedido.PAGO.toString(), itemPedidoResponseList);

        String itemPedidoRequestListJson = objectMapper.writeValueAsString(itemPedidoRequestList);


        when(clienteService.adicionarProdutoAPedido(eq(pedidoId), eq(itemPedidoRequestList), eq(emptyMap))).thenReturn(pedidoResponse);

        mockMvc.perform(post("/api/v1/clientes/pedidos/{pedidoId}/update/register", pedidoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemPedidoRequestListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(pedidoResponse.getPedidoId()))
                .andExpect(jsonPath("$.cliente").value(pedidoResponse.getCliente()))
                .andExpect(jsonPath("$.dataPedido[0]").value(pedidoResponse.getDataPedido().getYear()))
                .andExpect(jsonPath("$.dataPedido[1]").value(pedidoResponse.getDataPedido().getMonthValue()))
                .andExpect(jsonPath("$.dataPedido[2]").value(pedidoResponse.getDataPedido().getDayOfMonth()))
                .andExpect(jsonPath("$.dataPedido[3]").value(pedidoResponse.getDataPedido().getHour()))
                .andExpect(jsonPath("$.dataPedido[4]").value(pedidoResponse.getDataPedido().getMinute()))
                .andExpect(jsonPath("$.dataPedido[5]").value(pedidoResponse.getDataPedido().getSecond()))
                .andExpect(jsonPath("$.dataPedido[6]").value(pedidoResponse.getDataPedido().getNano()))
                .andExpect(jsonPath("$.valorPedido").value(pedidoResponse.getValorPedido()))
                .andExpect(jsonPath("$.statusPedido").value(pedidoResponse.getStatusPedido()))
                .andExpect(jsonPath("$.itensPedidos").isArray())
                .andExpect(jsonPath("$.itensPedidos[0].itemPedidoId").value(itemPedidoResponse.getItemPedidoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produtoId").value(itemPedidoResponse.getProdutoId()))
                .andExpect(jsonPath("$.itensPedidos[0].produto").value(itemPedidoResponse.getProduto()))
                .andExpect(jsonPath("$.itensPedidos[0].quantidade").value(itemPedidoResponse.getQuantidade()))
                .andExpect(jsonPath("$.itensPedidos[0].valorTotal").value(itemPedidoResponse.getValorTotal()))
                .andExpect(jsonPath("$.itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(clienteService, times(1)).adicionarProdutoAPedido(eq(pedidoId), eq(itemPedidoRequestList), eq(emptyMap));
    }

    @DisplayName("Deve retornar AdicionarProdutoAPedidoException ao tentar adicionar produto a pedido com erros")
    @Test
    public void testAdicionarProdutoAPedidoError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar adicionar produto a um pedido";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Status", "Status do pedido inválido para adicionar produto");
        AdicionarProdutoAPedidoException adicionarProdutoAPedidoException = new AdicionarProdutoAPedidoException(errorMessage, errors);

        Long pedidoIdError = 100L;
        ItemPedidoRequest itemPedidoRequestError = ControllersFixture.createItemPedidoRequest(-24L, 5);
        List<ItemPedidoRequest> itemPedidoRequestListError = List.of(itemPedidoRequestError);

        String itemPedidoRequestListErrorJson = objectMapper.writeValueAsString(itemPedidoRequestListError);

        when(clienteService.adicionarProdutoAPedido(eq(pedidoIdError), eq(itemPedidoRequestListError), eq(emptyMap))).thenThrow(adicionarProdutoAPedidoException);

        mockMvc.perform(post("/api/v1/clientes/pedidos/{pedidoId}/update/register", pedidoIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemPedidoRequestListErrorJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")));

        verify(clienteService, times(1)).adicionarProdutoAPedido(eq(pedidoIdError), eq(itemPedidoRequestListError), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso mensagem ao deletar item de pedido")
    @Test
    public void testDeletarItemPedidoSucesso() throws Exception {
        Long itemPedidoId = 2L;
        String messageSucesso = "usuario seu pedido foi deletado com sucesso";

        when(clienteService.deletarItemPedido(eq(itemPedidoId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/clientes/pedidos/itensPedidos/{itemPedidoId}/delete", itemPedidoId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(clienteService, times(1)).deletarItemPedido(eq(itemPedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar ItemPedidoNotFoundException ao tentar deletar item de pedido inexistente")
    @Test
    public void testDeletarItemPedidoError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar buscar pelo item pedido";
        errors.put("Falha de busca", "Item não encontrado");
        ItemPedidoNotFoundException itemPedidoNotFoundException = new ItemPedidoNotFoundException(errorMessage, errors);

        Long itemPedidoIdError = 4L;

        when(clienteService.deletarItemPedido(eq(itemPedidoIdError), eq(emptyMap))).thenThrow(itemPedidoNotFoundException);

        mockMvc.perform(delete("/api/v1/clientes/pedidos/itensPedidos/{itemPedidoId}/delete", itemPedidoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(clienteService, times(1)).deletarItemPedido(eq(itemPedidoIdError), eq(emptyMap));
    }

    @DisplayName("Deve retornar InvalidStatusPedidoException ao tentar deletar item de pedido com status inválido")
    @Test
    public void testDeletarItemPedidoError2() throws Exception {
        String errorMessage =  "usuario houve um erro ao tentar deletar seu item pedido";
        errors.put("Status", "Status do pedido inválido para deletar item pedido");
        InvalidStatusPedidoException invalidStatusPedidoException = new InvalidStatusPedidoException(errorMessage, errors);

        Long itemPedidoId = 1L;

        when(clienteService.deletarItemPedido(eq(itemPedidoId), eq(emptyMap))).thenThrow(invalidStatusPedidoException);

        mockMvc.perform(delete("/api/v1/clientes/pedidos/itensPedidos/{itemPedidoId}/delete", itemPedidoId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")));

        verify(clienteService, times(1)).deletarItemPedido(eq(itemPedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso mensagem ao deletar pedido")
    @Test
    public void testDeletarPedidoSucesso() throws Exception {
        Long pedidoId = 4L;
        String messageSucesso = "usuario seu pedido foi deletado com sucesso";

        when(clienteService.deletarPedido(eq(pedidoId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/clientes/pedidos/{pedidoId}/delete", pedidoId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(clienteService, times(1)).deletarPedido(eq(pedidoId), eq(emptyMap));
    }

    @DisplayName("Deve retornar DeletarPedidoException ao tentar deletar pedido inexistente e com status invalido")
    @Test
    public void testDeletarPedidoError() throws Exception {
        String errorMessage = "usuario houve um erro ao tentar deletar seu pedido";
        errors.put("Falha de busca", "Item não encontrado");
        errors.put("Status", "Status do pedido inválido para deletar item pedido");
        DeletarPedidoException deletarPedidoException = new DeletarPedidoException(errorMessage, errors);

        Long pedidoIdError = 43L;

        when(clienteService.deletarPedido(eq(pedidoIdError), eq(emptyMap))).thenThrow(deletarPedidoException);

        mockMvc.perform(delete("/api/v1/clientes/pedidos/{pedidoId}/delete", pedidoIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")))
                .andExpect(jsonPath("$.fieldsErrors['Status']").value(errors.get("Status")));

        verify(clienteService, times(1)).deletarPedido(eq(pedidoIdError), eq(Collections.emptyMap()));
    }
}
