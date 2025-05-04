package br.com.thaua.Ecommerce.controllers.admin;

import br.com.thaua.Ecommerce.controllers.AdminController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.exceptions.AddressNotFoundException;
import br.com.thaua.Ecommerce.exceptions.CategoriaNotFoundException;
import br.com.thaua.Ecommerce.exceptions.PedidoNotFoundException;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.services.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.mail.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.extension.ExtendWith;
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    private Map<String, String> errors;

    private Map<String, String> emptyMap;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        errors = ConstructorErrors.returnMapErrors();
        emptyMap = ConstructorErrors.returnMapErrors();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Deve listar clientes com sucesso")
    @Test
    public void testListarClientesSucesso() throws Exception {
        ClienteResponse clienteResponse = Fixture.createClienteResponse(1L, "klovis", "klovis@gmail.com", "840940423334", "000.000.000-00");
        List<ClienteResponse> clienteResponseList = List.of(clienteResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<ClienteResponse> page = new PageImpl(clienteResponseList, pageable, clienteResponseList.size());

        Pagina<ClienteResponse> clienteResponsePagina = new Pagina<>();
        clienteResponsePagina.setConteudo(clienteResponseList);
        clienteResponsePagina.setPaginaAtual(pageable.getPageNumber());
        clienteResponsePagina.setTotalPaginas(page.getTotalPages());
        clienteResponsePagina.setItensPorPagina(pageable.getPageSize());
        clienteResponsePagina.setTotalItens(page.getTotalElements());
        clienteResponsePagina.setUltimaPagina(true);

        when(adminService.listarClientes(eq(pageable))).thenReturn(clienteResponsePagina);

        mockMvc.perform(get("/api/v1/admin/clientes")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].id").value(clienteResponse.getId()))
                .andExpect(jsonPath("$.conteudo[0].name").value(clienteResponse.getName()))
                .andExpect(jsonPath("$.conteudo[0].email").value(clienteResponse.getEmail()))
                .andExpect(jsonPath("$.conteudo[0].telefone").value(clienteResponse.getTelefone()))
                .andExpect(jsonPath("$.conteudo[0].cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("$.paginaAtual").value(clienteResponsePagina.getPaginaAtual()))
                .andExpect(jsonPath("$.totalPaginas").value(clienteResponsePagina.getTotalPaginas()))
                .andExpect(jsonPath("$.itensPorPagina").value(clienteResponsePagina.getItensPorPagina()))
                .andExpect(jsonPath("$.totalItens").value(clienteResponsePagina.getTotalItens()))
                .andExpect(jsonPath("$.ultimaPagina").value(clienteResponsePagina.getUltimaPagina()));

        verify(adminService, times(1)).listarClientes(eq(pageable));
    }

    @DisplayName("Deve retornar um cliente com sucesso")
    @Test
    public void testBuscarClienteSucesso() throws Exception {
        Long clienteId = 2L;
        ClienteResponse clienteResponse = Fixture.createClienteResponse(clienteId, "adan", "adan@gmail.com", "84094034244", "000.000.000-00");

        when(adminService.buscarCliente(eq(clienteId), eq(emptyMap))).thenReturn(clienteResponse);

        mockMvc.perform(get("/api/v1/admin/clientes/{clienteId}/list", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId()))
                .andExpect(jsonPath("$.name").value(clienteResponse.getName()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()))
                .andExpect(jsonPath("$.telefone").value(clienteResponse.getTelefone()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()));

        verify(adminService, times(1)).buscarCliente(eq(clienteId), eq(emptyMap));
    }

    @DisplayName("Deve retornar UserNotFoundException ao tentar buscar cliente")
    @Test
    public void testBuscarClienteError() throws Exception {
        String errorMessage ="Houve um erro na hora de buscar o cliente";
        errors.put("Falha de busca", "Item não encontrado");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, errors);

        Long clienteIdError = -2L;

        when(adminService.buscarCliente(eq(clienteIdError), eq(emptyMap))).thenThrow(userNotFoundException);

        mockMvc.perform(get("/api/v1/admin/clientes/{clienteId}/list", clienteIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).buscarCliente(eq(clienteIdError), eq(emptyMap));
    }

    @DisplayName("Deve retornar um fornecedor com sucesso")
    @Test
    public void testBuscarFornecedorSucesso() throws Exception {
        Long fornecedorId = 2L;
        FornecedorResponse fornecedorResponse = Fixture.createFornecedorResponse(fornecedorId, "jonson", "jonson@gmail.com", "234423233", "000.000.000/0000-00");

        when(adminService.buscarFornecedor(eq(fornecedorId), eq(emptyMap))).thenReturn(fornecedorResponse);

        mockMvc.perform(get("/api/v1/admin/fornecedores/{fornecedorId}/list", fornecedorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fornecedorResponse.getId()))
                .andExpect(jsonPath("$.name").value(fornecedorResponse.getName()))
                .andExpect(jsonPath("$.email").value(fornecedorResponse.getEmail()))
                .andExpect(jsonPath("$.telefone").value(fornecedorResponse.getTelefone()))
                .andExpect(jsonPath("$.cnpj").value(fornecedorResponse.getCnpj()));

        verify(adminService, times(1)).buscarFornecedor(eq(fornecedorId), eq(emptyMap));
    }

    @DisplayName("Deve retornar UserNotFoundException ao tentar buscar fornecedor")
    @Test
    public void testBuscarFornecedorError() throws Exception {
        String errorMessage = "Houve um erro na hora de buscar o fornecedor";
        errors.put("Falha de busca", "Item não encontrado");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, errors);

        Long fornecedorIdError = -5L;

        when(adminService.buscarFornecedor(eq(fornecedorIdError), eq(emptyMap))).thenThrow(userNotFoundException);

        mockMvc.perform(get("/api/v1/admin/fornecedores/{fornecedorId}/list", fornecedorIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).buscarFornecedor(eq(fornecedorIdError), eq(emptyMap));
    }

    @DisplayName("Deve listar fornecedores com sucesso")
    @Test
    public void testListarFornecedoresSucesso() throws Exception {
        FornecedorResponse fornecedorResponse = Fixture.createFornecedorResponse(2L, "jonson", "jonson@gmail.com", "234423233", "000.000.000/0000-00");
        List<FornecedorResponse> fornecedorResponseList = List.of(fornecedorResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<ClienteResponse> page = new PageImpl(fornecedorResponseList, pageable, fornecedorResponseList.size());

        Pagina<FornecedorResponse> fornecedorResponsePagina = new Pagina<>();
        fornecedorResponsePagina.setConteudo(fornecedorResponseList);
        fornecedorResponsePagina.setPaginaAtual(pageable.getPageNumber());
        fornecedorResponsePagina.setTotalPaginas(page.getTotalPages());
        fornecedorResponsePagina.setItensPorPagina(pageable.getPageSize());
        fornecedorResponsePagina.setTotalItens(page.getTotalElements());
        fornecedorResponsePagina.setUltimaPagina(true);

        when(adminService.listarFornecedores(eq(pageable))).thenReturn(fornecedorResponsePagina);

        mockMvc.perform(get("/api/v1/admin/fornecedores/list")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].id").value(fornecedorResponse.getId()))
                .andExpect(jsonPath("$.conteudo[0].name").value(fornecedorResponse.getName()))
                .andExpect(jsonPath("$.conteudo[0].email").value(fornecedorResponse.getEmail()))
                .andExpect(jsonPath("$.conteudo[0].telefone").value(fornecedorResponse.getTelefone()))
                .andExpect(jsonPath("$.conteudo[0].cnpj").value(fornecedorResponse.getCnpj()))
                .andExpect(jsonPath("$.paginaAtual").value(fornecedorResponsePagina.getPaginaAtual()))
                .andExpect(jsonPath("$.totalPaginas").value(fornecedorResponsePagina.getTotalPaginas()))
                .andExpect(jsonPath("$.itensPorPagina").value(fornecedorResponsePagina.getItensPorPagina()))
                .andExpect(jsonPath("$.totalItens").value(fornecedorResponsePagina.getTotalItens()))
                .andExpect(jsonPath("$.ultimaPagina").value(fornecedorResponsePagina.getUltimaPagina()));

        verify(adminService, times(1)).listarFornecedores(eq(pageable));
    }

    @DisplayName("Deve listar admins com sucesso")
    @Test
    public void testListarAdminsSucesso() throws Exception {
        AdminResponse adminResponse = Fixture.createAdminResponse(1L, "taagashi", "taagashi@gmail.com", 4, LocalDateTime.now());
        List<AdminResponse> adminResponseList = List.of(adminResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<AdminResponse> page = new PageImpl(adminResponseList, pageable, adminResponseList.size());

        Pagina<AdminResponse> adminResponsePagina = new Pagina<>();
        adminResponsePagina.setConteudo(adminResponseList);
        adminResponsePagina.setPaginaAtual(pageable.getPageNumber());
        adminResponsePagina.setTotalPaginas(page.getTotalPages());
        adminResponsePagina.setItensPorPagina(pageable.getPageSize());
        adminResponsePagina.setTotalItens(page.getTotalElements());
        adminResponsePagina.setUltimaPagina(true);

        when(adminService.listarAdmins(eq(pageable))).thenReturn(adminResponsePagina);

        mockMvc.perform(get("/api/v1/admin/list")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.conteudo[0].id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.conteudo[0].name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.conteudo[0].email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.conteudo[0].contasBanidas").value(adminResponse.getContasBanidas()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[0]").value(adminResponse.getUltimoAcesso().getYear()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[1]").value(adminResponse.getUltimoAcesso().getMonthValue()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[2]").value(adminResponse.getUltimoAcesso().getDayOfMonth()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[3]").value(adminResponse.getUltimoAcesso().getHour()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[4]").value(adminResponse.getUltimoAcesso().getMinute()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[5]").value(adminResponse.getUltimoAcesso().getSecond()))
                .andExpect(jsonPath("$.conteudo[0].ultimoAcesso[6]").value(adminResponse.getUltimoAcesso().getNano()))
                .andExpect(jsonPath("$.paginaAtual").value(adminResponsePagina.getPaginaAtual()))
                .andExpect(jsonPath("$.totalPaginas").value(adminResponsePagina.getTotalPaginas()))
                .andExpect(jsonPath("$.itensPorPagina").value(adminResponsePagina.getItensPorPagina()))
                .andExpect(jsonPath("$.totalItens").value(adminResponsePagina.getTotalItens()))
                .andExpect(jsonPath("$.ultimaPagina").value(adminResponsePagina.getUltimaPagina()));

        verify(adminService, times(1)).listarAdmins(eq(pageable));
    }

    @DisplayName("Deve retornar um admin com sucesso")
    @Test
    public void testBuscarAdminSucesso() throws Exception {
        Long adminId = 5L;
        AdminResponse adminResponse = Fixture.createAdminResponse(1L, "taagashi", "taagashi@gmail.com", 4, LocalDateTime.now());

        when(adminService.buscarAdmin(eq(adminId), eq(emptyMap))).thenReturn(adminResponse);

        mockMvc.perform(get("/api/v1/admin/{adminId}/list", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.contasBanidas").value(adminResponse.getContasBanidas()))
                .andExpect(jsonPath("$.ultimoAcesso[0]").value(adminResponse.getUltimoAcesso().getYear()))
                .andExpect(jsonPath("$.ultimoAcesso[1]").value(adminResponse.getUltimoAcesso().getMonthValue()))
                .andExpect(jsonPath("$.ultimoAcesso[2]").value(adminResponse.getUltimoAcesso().getDayOfMonth()))
                .andExpect(jsonPath("$.ultimoAcesso[3]").value(adminResponse.getUltimoAcesso().getHour()))
                .andExpect(jsonPath("$.ultimoAcesso[4]").value(adminResponse.getUltimoAcesso().getMinute()))
                .andExpect(jsonPath("$.ultimoAcesso[5]").value(adminResponse.getUltimoAcesso().getSecond()))
                .andExpect(jsonPath("$.ultimoAcesso[6]").value(adminResponse.getUltimoAcesso().getNano()));

        verify(adminService, times(1)).buscarAdmin(eq(adminId), eq(emptyMap));
    }

    @DisplayName("Deve retornar UserNotFoundException ao tentar buscar admin")
    @Test
    public void testBuscarAdminError() throws Exception {
        String errorMessage = "Houve um erro na hora de buscar admin";
        errors.put("Falha de busca", "Item não encontrado");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, errors);

        Long adminIdError = 245L;

        when(adminService.buscarAdmin(eq(adminIdError), eq(emptyMap))).thenThrow(userNotFoundException);

        mockMvc.perform(get("/api/v1/admin/{adminId}/list", adminIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).buscarAdmin(eq(adminIdError), eq(emptyMap));
    }

    @DisplayName("Deve cadastrar uma categoria com sucesso")
    @Test
    public void testCadastrarCategoriaSucesso() throws Exception {
        CategoriaRequest categoriaRequest = Fixture.createCategoriaRequest("esporte", "categoria para esporte");
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(1L, categoriaRequest.getNome(), categoriaRequest.getDescricao(), 29);

        String categoriaRequestJson = objectMapper.writeValueAsString(categoriaRequest);

        when(adminService.cadastrarNovaCategoria(eq(categoriaRequest))).thenReturn(categoriaResponse);

        mockMvc.perform(post("/api/v1/admin/categorias/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaRequestJson))
                .andExpect(jsonPath("$.id").value(categoriaResponse.getId()))
                .andExpect(jsonPath("$.nome").value(categoriaResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(categoriaResponse.getDescricao()))
                .andExpect(jsonPath("$.produtosAssociados").value(categoriaResponse.getProdutosAssociados()));

        verify(adminService, times(1)).cadastrarNovaCategoria(eq(categoriaRequest));
    }

    @DisplayName("Deve remover um usuario com sucesso")
    @Test
    public void testRemoverUsuarioSucesso() throws Exception {
        Long userId = 4L;
        String messageSucesso = "usuario foi removido com sucesso";

        when(adminService.removerUsuario(eq(userId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/admin/users/{userId}/delete", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));
    }

    @DisplayName("Deve retornar UserNotFoundException ao tentar remover usuario")
    @Test
    public void testRemoverUsuarioError() throws Exception {
        String errorMessage = "usuario houve um erro para remover conta de cliente";
        errors.put("Falha de busca", "Item não encontrado");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, errors);

        Long userIdError = -23L;

        when(adminService.removerUsuario(eq(userIdError), eq(emptyMap))).thenThrow(userNotFoundException);

        mockMvc.perform(delete("/api/v1/admin/users/{userId}/delete", userIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).removerUsuario(eq(userIdError), eq(emptyMap));
    }

    @DisplayName("Deve listar pedidos do cliente com sucesso")
    @Test
    public void testListarPedidoDoClienteSucesso() throws Exception {
        Long clienteId = 90L;

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(1L, 1L, "Camiseta", 5, BigDecimal.valueOf(20.55), StatusItemPedido.PENDENTE);
        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(1L, "marcos", LocalDateTime.now(), itemPedidoResponse.getValorTotal(), StatusPedido.AGUARDANDO_PAGAMENTO.toString(), List.of(itemPedidoResponse));
        List<PedidoResponse> pedidoResponseList = List.of(pedidoResponse);

        Pageable pageable = PageRequest.of(0 , 1);
        Page page = new PageImpl(pedidoResponseList, pageable, pedidoResponseList.size());

        Pagina<PedidoResponse> pedidoResponsePagina = new Pagina<>();
        pedidoResponsePagina.setConteudo(pedidoResponseList);
        pedidoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        pedidoResponsePagina.setTotalPaginas(page.getTotalPages());
        pedidoResponsePagina.setItensPorPagina(pageable.getPageSize());
        pedidoResponsePagina.setTotalItens((long) pedidoResponseList.size());
        pedidoResponsePagina.setUltimaPagina(true);

        when(adminService.listarPedidosDoCliente(eq(clienteId), eq(pageable), eq(errors))).thenReturn(pedidoResponsePagina);

        mockMvc.perform(get("/api/v1/admin/clientes/{clienteId}/pedidos/list", clienteId)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
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
                .andExpect(jsonPath("$.conteudo[0].itensPedidos[0].statusItemPedido").value(itemPedidoResponse.getStatusItemPedido().toString()));

        verify(adminService, times(1)).listarPedidosDoCliente(eq(clienteId), eq(pageable), eq(emptyMap));
    }

    @DisplayName("Deve retornar PedidoNotFoundException ao tentar listar pedidos do cliente")
    @Test
    public void testListarPedidoDoClienteError() throws Exception {
        String errorMessage = "Houve um erro na hora de buscar os pedidos do cliente";
        errors.put("Falha de busca", "Item não encontrado");
        PedidoNotFoundException pedidoNotFoundException = new PedidoNotFoundException(errorMessage, errors);

        Pageable pageable = PageRequest.of(0, 1);

        Long clienteIdError = -4L;

        when(adminService.listarPedidosDoCliente(eq(clienteIdError), eq(pageable), eq(emptyMap))).thenThrow(pedidoNotFoundException);

        mockMvc.perform(get("/api/v1/admin/clientes/{clienteId}/pedidos/list", clienteIdError)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).listarPedidosDoCliente(eq(clienteIdError), eq(pageable), eq(emptyMap));
    }

    @DisplayName("Deve atualizar status do pedido com sucesso")
    @Test
    public void testAtualizarStatusPedidoSucesso() throws Exception {
        Long pedidoId = 6L;
        PedidoPatchRequest pedidoPatchRequest = Fixture.createPedidoPatchRequest(StatusPedido.PAGO);
        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(1L, 1L, "garrafa", 5, BigDecimal.valueOf(20.55), StatusItemPedido.PROCESSANDO);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse( pedidoId, "marcos", LocalDateTime.now(), itemPedidoResponse.getValorTotal(), StatusPedido.PAGO.toString(), itemPedidoResponseList);

        String pedidoPatchRequestJson = objectMapper.writeValueAsString(pedidoPatchRequest);

        when(adminService.atualizarStatusPedido(eq(pedidoId), eq(pedidoPatchRequest), eq(emptyMap))).thenReturn(pedidoResponse);

        mockMvc.perform(patch("/api/v1/admin/pedidos/{pedidoId}/status/update", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pedidoPatchRequestJson))
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

        verify(adminService, times(1)).atualizarStatusPedido(eq(pedidoId), eq(pedidoPatchRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar PedidoNotFoundException ao tentar atualizar status do pedido")
    @Test
    public void testAtualizarStatusPedidoError() throws Exception {
        String errorMessage = "Houve um erro ao atualizar pedido";
        errors.put("Falha de busca", "Item não encontrado");
        PedidoNotFoundException pedidoNotFoundException = new PedidoNotFoundException(errorMessage, errors);

        Long pedidoIdError = -323L;
        PedidoPatchRequest pedidoPatchRequest = Fixture.createPedidoPatchRequest(StatusPedido.PAGO);

        String pedidoPatchRequestJson = objectMapper.writeValueAsString(pedidoPatchRequest);

        when(adminService.atualizarStatusPedido(eq(pedidoIdError), eq(pedidoPatchRequest), eq(emptyMap))).thenThrow(pedidoNotFoundException);

        mockMvc.perform(patch("/api/v1/admin/pedidos/{pedidoId}/status/update", pedidoIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pedidoPatchRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).atualizarStatusPedido(eq(pedidoIdError), eq(pedidoPatchRequest), eq(emptyMap));
    }

    @DisplayName("Deve cadastrar endereco do usuario com sucesso")
    @Test
    public void testCadastrarEnderecoUsuarioSucesso() throws Exception {
        Long userId = 4L;
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Raimundo de Brito", "79", "Centro", "Cidade", "RN", "48323341");
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, "taagashi", enderecoRequest.getRua(), enderecoRequest.getNumero(), enderecoRequest.getBairro(), enderecoRequest.getCidade(), enderecoRequest.getEstado(), enderecoRequest.getCep());

        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);

        when(adminService.cadastrarEnderecoUsuario(eq(userId), eq(enderecoRequest), eq(emptyMap))).thenReturn(enderecoResponse);

        mockMvc.perform(post("/api/v1/admin/users/{userId}/enderecos/register", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.cep").value(enderecoResponse.getCep()));

        verify(adminService, times(1)).cadastrarEnderecoUsuario(eq(userId), eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar AddressNotFoundException ao tentar cadastrar endereco do usuario")
    @Test
    public void testCadastrarEnderecoUsuarioError() throws Exception {
        String errorMessage = "Houve um erro ao cadastrar endereco para usuario";
        errors.put("Falha de busca", "Item não encontrado");
        AddressNotFoundException addressNotFoundException = new AddressNotFoundException(errorMessage, errors);

        Long userIdError = -2L;
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Olinda Santa", "25", "Centro", "Cidade", "ES", "48323341");

        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);

        when(adminService.cadastrarEnderecoUsuario(eq(userIdError), eq(enderecoRequest), eq(emptyMap))).thenThrow(addressNotFoundException);

        mockMvc.perform(post("/api/v1/admin/users/{userId}/enderecos/register", userIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).cadastrarEnderecoUsuario(eq(userIdError), eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve exibir endereco do usuario com sucesso")
    @Test
    public void testExibirEnderecoUsuarioSucesso() throws Exception {
        Long userId = 2L;
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, "marcos", "maringa", "2", "bairro", "cidade", "sul", "1233324");

        when(adminService.exibirEnderecoUsuario(eq(userId), eq(emptyMap))).thenReturn(enderecoResponse);

        mockMvc.perform(get("/api/v1/admin/users/{userId}/endereco/list", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.cep").value(enderecoResponse.getCep()));

        verify(adminService, times(1)).exibirEnderecoUsuario(eq(userId), eq(emptyMap));
    }

    @DisplayName("Deve retornar AddressNotFoundException ao tentar exibir endereco do usuario")
    @Test
    public void testExibirEnderecoUsuarioError() throws Exception {
        String errorMessage = "Houve um erro ao exibir endereco de usuario";
        errors.put("Falha de busca", "Item não encontrado");
        AddressNotFoundException addressNotFoundException = new AddressNotFoundException(errorMessage, errors);

        Long userIdError = -45L;

        when(adminService.exibirEnderecoUsuario(eq(userIdError), eq(emptyMap))).thenThrow(addressNotFoundException);

        mockMvc.perform(get("/api/v1/admin/users/{userId}/endereco/list", userIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).exibirEnderecoUsuario(eq(userIdError), eq(emptyMap));
    }

    @DisplayName("Deve atualizar endereco do usuario com sucesso")
    @Test
    public void testAtualizarEnderecoUsuarioSucesso() throws Exception {
        Long userId = 100L;
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Raimundo de Brito", "79", "Centro", "Cidade", "RN", "48323341");
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, "taagashi", enderecoRequest.getRua(), enderecoRequest.getNumero(), enderecoRequest.getBairro(), enderecoRequest.getCidade(), enderecoRequest.getEstado(), enderecoRequest.getCep());

        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);

        when(adminService.atualizarEnderecoUsuario(eq(userId), eq(enderecoRequest), eq(emptyMap))).thenReturn(enderecoResponse);

        mockMvc.perform(put("/api/v1/admin/users/{userId}/enderecos/update", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.cep").value(enderecoResponse.getCep()));

        verify(adminService, times(1)).atualizarEnderecoUsuario(eq(userId), eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar AddressNotFoundException ao tentar atualizar endereco do usuario")
    @Test
    public void testAtualizarEnderecoUsuarioError() throws Exception {
        String errorMessage = "Houve um erro ao atualizar endereco de usuario";
        errors.put("Falha de busca", "Item não encontrado");
        AddressNotFoundException addressNotFoundException = new AddressNotFoundException(errorMessage, errors);

        Long userIdError = -2L;
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Olinda Santa", "25", "Centro", "Cidade", "ES", "48323341");

        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);

        when(adminService.atualizarEnderecoUsuario(eq(userIdError), eq(enderecoRequest), eq(emptyMap))).thenThrow(addressNotFoundException);

        mockMvc.perform(put("/api/v1/admin/users/{userId}/enderecos/update", userIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).atualizarEnderecoUsuario(eq(userIdError), eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve deletar endereco do usuario com sucesso")
    @Test
    public void testDeletarEnderecoUsuario() throws Exception {
        Long userId = 92L;
        String messageSucesso = "usuario teve seu endereco limpo com sucesso";

        when(adminService.deletarEnderecoUsuario(eq(userId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/admin/users/{userId}/enderecos/delete", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(adminService, times(1)).deletarEnderecoUsuario(eq(userId), eq(emptyMap));
    }

    @DisplayName("Deve retornar AddressNotFoundException ao tentar deletar endereco do usuario")
    @Test
    public void testDeletarEnderecoUsuarioError() throws Exception {
        String errorMessage = "Houve um erro ao tentar deletar endereco de usuario";
        errors.put("Falha de busca", "Item não encontrado");
        AddressNotFoundException addressNotFoundException = new AddressNotFoundException(errorMessage, errors);

        Long userIdError = 9L;
        when(adminService.deletarEnderecoUsuario(eq(userIdError), eq(emptyMap))).thenThrow(addressNotFoundException);

        mockMvc.perform(delete("/api/v1/admin/users/{userId}/enderecos/delete", userIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).deletarEnderecoUsuario(eq(userIdError), eq(emptyMap));
    }

    @DisplayName("Deve atualizar categoria com sucesso")
    @Test
    public void testAtualizarCategoriaSucesso() throws Exception {
        Long categoriaId = 5L;

        CategoriaRequest categoriaRequest = Fixture.createCategoriaRequest("tecnologia", "categoria para tecnologia");
        CategoriaResponse categoriaResponse = Fixture.createCategoriaResponse(categoriaId, categoriaRequest.getNome(), categoriaRequest.getDescricao(), 29);

        String categoriaRequestJson = objectMapper.writeValueAsString(categoriaRequest);

        when(adminService.atualizarCategoria(eq(categoriaId), eq(categoriaRequest), eq(emptyMap))).thenReturn(categoriaResponse);

        mockMvc.perform(put("/api/v1/admin/categorias/{categoriaId}/update", categoriaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoriaRequestJson))
                .andExpect(jsonPath("$.id").value(categoriaResponse.getId()))
                .andExpect(jsonPath("$.nome").value(categoriaResponse.getNome()))
                .andExpect(jsonPath("$.descricao").value(categoriaResponse.getDescricao()))
                .andExpect(jsonPath("$.produtosAssociados").value(categoriaResponse.getProdutosAssociados()));

        verify(adminService, times(1)).atualizarCategoria(eq(categoriaId), eq(categoriaRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar CategoriaNotFoundException ao tentar atualizar categoria")
    @Test
    public void testAtualizarCategoriaError() throws Exception {
        String errorMessage = "Houve um erro ao tentar atualizar categoria";
        errors.put("Falha de busca", "Item não encontrado");
        CategoriaNotFoundException categoriaNotFoundException = new CategoriaNotFoundException(errorMessage, errors);


        Long categoriaIdError = -2L;
        CategoriaRequest categoriaRequest = Fixture.createCategoriaRequest("tecnologia", "categoria para tecnologia");


        String categoriaRequestJson = objectMapper.writeValueAsString(categoriaRequest);

        when(adminService.atualizarCategoria(eq(categoriaIdError), eq(categoriaRequest), eq(emptyMap))).thenThrow(categoriaNotFoundException);

        mockMvc.perform(put("/api/v1/admin/categorias/{categoriaId}/update", categoriaIdError)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaRequestJson))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).atualizarCategoria(eq(categoriaIdError), eq(categoriaRequest), eq(emptyMap));
    }

    @DisplayName("Deve deletar categoria com sucesso")
    @Test
    public void testDeletarCategoriaSucesso() throws Exception {
        Long categoriaId = 24L;
        String messageSucesso = "categoria foi deletada com sucesso";

        when(adminService.deletarCategoria(eq(categoriaId), eq(emptyMap))).thenReturn(messageSucesso);

        mockMvc.perform(delete("/api/v1/admin/categorias/{categoriaId}/delete", categoriaId))
                .andExpect(status().isOk())
                .andExpect(content().string(messageSucesso));

        verify(adminService, times(1)).deletarCategoria(eq(categoriaId), eq(emptyMap));
    }

    @DisplayName("Deve retornar CategoriaNotFoundException ao tentar deletar categoria")
    @Test
    public void testDeletarCategoriaError() throws Exception {
        String errorMessage = "Houve um erro ao tentar deletar categoria";
        errors.put("Falha de busca", "Item não encontrado");
        CategoriaNotFoundException categoriaNotFoundException = new CategoriaNotFoundException(errorMessage, errors);

        Long categoriaIdError = 20L;

        when(adminService.deletarCategoria(eq(categoriaIdError), eq(emptyMap))).thenThrow(categoriaNotFoundException);

        mockMvc.perform(delete("/api/v1/admin/categorias/{categoriaId}/delete", categoriaIdError))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Falha de busca']").value(errors.get("Falha de busca")));

        verify(adminService, times(1)).deletarCategoria(eq(categoriaIdError), eq(emptyMap));
    }
}
