package br.com.thaua.Ecommerce.services.cliente;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.Role;
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
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.mappers.ItemPedidoMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.PedidoMapper;
import br.com.thaua.Ecommerce.repositories.ItemPedidoRepository;
import br.com.thaua.Ecommerce.repositories.PedidoRepository;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.ClienteService;
import br.com.thaua.Ecommerce.services.JWTService;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoMapper itemPedidoMapper;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private PaginaMapper paginaMapper;

    @Mock
    private JWTService jwtService;

    @Mock
    private ExtractTypeUserContextHolder extractTypeUserContextHolder;

    private Map<String, String> errors;

    private ClienteEntity clienteEntity;
    @BeforeEach
    public void setUp() {
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve atualizar cpf e telefone do cliente com sucesso")
    @Test
    public void testAtualizarCpfETelefoneSucesso() {
        Long userId = 4L;
        String name = "marcos";
        String telefone = "+000000-0000";
        String email = "marcos@gmail.com";
        String password = "senha";
        Role role = Role.CLIENTE;
        String cpf = "000.000.000-00";
        ClienteCpfTelefoneRequest clienteCpfTelefoneRequest = Fixture.createClienteCpfTelefoneRequest(cpf, telefone);

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        ClienteEntity clienteEntity = Fixture.createclienteEntity(usersEntity.getId(), usersEntity, cpf, null);

        usersEntity.setCliente(clienteEntity);

        usersEntity.setCliente(clienteEntity);
        ClienteResponse clienteResponse = Fixture.createClienteResponse(usersEntity.getId(), usersEntity.getName(), usersEntity.getEmail(), usersEntity.getTelefone(), cpf);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(clienteMapper.toResponse(clienteEntity)).thenReturn(clienteResponse);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);

        ClienteResponse clienteResponseCompare = clienteService.atualizarCpfETelefone(clienteCpfTelefoneRequest, errors);

        assertThat(clienteResponseCompare.getId()).isEqualTo(clienteResponse.getId());
        assertThat(clienteResponseCompare.getName()).isEqualTo(clienteResponse.getName());
        assertThat(clienteResponseCompare.getEmail()).isEqualTo(clienteResponse.getEmail());
        assertThat(clienteResponseCompare.getTelefone()).isEqualTo(clienteResponse.getTelefone());
        assertThat(clienteResponseCompare.getCpf()).isEqualTo(clienteResponse.getCpf());
    }

    @DisplayName("Deve retornar com sucesso token de acesso ao atualizar dados do cliente")
    @Test
    public void testAtualizarDadosSucesso() {
        Long userId = 4L;
        String name = "marcos";
        String telefone = "+000000-0000";
        String email = "marcos@gmail.com";
        String password = "senha";
        Role role = Role.CLIENTE;
        String cpf = "000.000.000-00";

        String jwtToken = "token";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, new ClienteEntity(), null, null);
        ClienteUpdateRequest clienteUpdateRequest = Fixture.createClienteUpdateRequest(name, email, telefone, cpf);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        when(jwtService.generateToken(any(MyUserDetails.class))).thenReturn(jwtToken);

        String jwtTokenCompare = clienteService.atualizarDados(clienteUpdateRequest);

        assertThat(jwtTokenCompare).isEqualTo(jwtToken);
    }

    @DisplayName("Deve retornar com sucesso pedido do cliente apos ele fazer seu pedido")
    @Test
    public void testFazerPedidoSucesso() {
        UsersEntity usersEntity = Fixture.createUsersEntity(1L, "antonio", "antonio@gmail.com", "+000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(1L, 2);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(1L, itemPedidoRequest.getProdutoId(), "tablet", itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(25.13), StatusItemPedido.PENDENTE);
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(itemPedidoResponse.getItemPedidoId(), itemPedidoResponse.getQuantidade(), itemPedidoResponse.getValorTotal(), null, null, itemPedidoResponse.getStatusItemPedido());
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(itemPedidoRequest.getProdutoId(), itemPedidoResponse.getProduto(), "produto de tablet", itemPedidoEntity.getValorTotal(), 4, null, itemPedidoEntityList, 2, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        itemPedidoEntity.setProduto(produtoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(1L, LocalDateTime.now(), itemPedidoResponse.getValorTotal(), StatusPedido.PAGO, null, itemPedidoEntityList);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoEntity.getId(), usersEntity.getName(), LocalDateTime.now(), pedidoEntity.getValorPedido(), pedidoEntity.getStatusPedido().toString(), itemPedidoResponseList);


        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);
        when(produtoRepository.findAllById(any(Iterable.class))).thenReturn(produtoEntityList);
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);
        when(pedidoMapper.toPedidoResponse(any(PedidoEntity.class))).thenReturn((pedidoResponse));

        PedidoResponse pedidoResponseCompare = clienteService.fazerPedido(itemPedidoRequestList, errors);

        assertThat(pedidoResponseCompare.getPedidoId()).isEqualTo(pedidoResponse.getPedidoId());
        assertThat(pedidoResponseCompare.getCliente()).isEqualTo(pedidoResponse.getCliente());
        assertThat(pedidoResponseCompare.getDataPedido()).isEqualTo(pedidoResponse.getDataPedido());
        assertThat(pedidoResponseCompare.getValorPedido()).isEqualTo(pedidoResponse.getValorPedido());
        assertThat(pedidoResponseCompare.getStatusPedido()).isEqualTo(pedidoResponse.getStatusPedido());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getItemPedidoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getItemPedidoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProdutoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProdutoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProduto()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProduto());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getQuantidade()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getQuantidade());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getValorTotal()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getValorTotal());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getStatusItemPedido()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getStatusItemPedido());
    }

    @DisplayName("Deve retornar FazerPedidoException apos falha ao fazer pedido")
    @Test
    public void testFazerPedidoError() {
        UsersEntity usersEntity = Fixture.createUsersEntity(12L, "bruno", "bruno@gmail.com", "+000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(1L, 1);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(3L, itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(24.2), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "pc", "produto de pc", itemPedidoEntity.getValorTotal(), 5, null, itemPedidoEntityList, 3, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        String errorMessage = usersEntity.getName() + ", houve um erro na hora de fazer um pedido";
        errors.put("Endereço", "Endereço não cadastrado");
        errors.put("Telefone", "Telefone não cadastrado");
        errors.put(produtoEntity.getNome(), "Quantidade acima do estoque ou igual a 0 para este produto");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);
        when(produtoRepository.findAllById(any(Iterable.class))).thenReturn(produtoEntityList);

        doThrow(new FazerPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, FazerPedidoException.class, errors);

        FazerPedidoException fazerPedidoException = assertThrows(FazerPedidoException.class, () -> clienteService.fazerPedido(itemPedidoRequestList, errors));

        assertThat(fazerPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(fazerPedidoException.getFields().get("Endereço")).isEqualTo(errors.get("Endereço"));
        assertThat(fazerPedidoException.getFields().get("Telefone")).isEqualTo(errors.get("Telefone"));
        assertThat(fazerPedidoException.getFields().get(produtoEntity.getNome())).isEqualTo(errors.get(produtoEntity.getNome()));
    }

    @DisplayName("Deve listar pedidos do cliente com sucesso")
    @Test
    public void testListarPedidosSucesso() {
        String statusPedido = "AGUARDANDO_PAGAMENTO";
        UsersEntity usersEntity = Fixture.createUsersEntity(2L, "bruno", "bruno@gmail.com", "+000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null );

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(1L, LocalDateTime.now(), BigDecimal.valueOf(123), StatusPedido.AGUARDANDO_PAGAMENTO, null, null);
        List<PedidoEntity> pedidoEntityList = List.of(pedidoEntity);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoEntity.getId(), usersEntity.getName(), LocalDateTime.now(), BigDecimal.valueOf(123), pedidoEntity.getStatusPedido().toString(), List.of());
        List<PedidoResponse> pedidoResponseList = List.of(pedidoResponse);

        Pageable pageable = PageRequest.of(0, 1);
        Page<PedidoResponse> pageResponse = new PageImpl<>(pedidoResponseList, pageable, pedidoResponseList.size());
        
        Page<PedidoEntity> pageEntity = new PageImpl<>(pedidoEntityList, pageable, pedidoEntityList.size());

        Pagina<PedidoEntity> pedidoEntityPagina = new Pagina<>();
        pedidoEntityPagina.setConteudo(pedidoEntityList);
        pedidoEntityPagina.setPaginaAtual(pageable.getPageNumber());
        pedidoEntityPagina.setTotalPaginas(pageEntity.getTotalPages());
        pedidoEntityPagina.setItensPorPagina(pageable.getPageSize());
        pedidoEntityPagina.setTotalItens(pageEntity.getTotalElements());
        pedidoEntityPagina.setUltimaPagina(true);

        Pagina<PedidoResponse> pedidoResponsePagina = new Pagina<>();
        pedidoResponsePagina.setConteudo(pedidoResponseList);
        pedidoResponsePagina.setPaginaAtual(pageable.getPageNumber());
        pedidoResponsePagina.setTotalPaginas(pageEntity.getTotalPages());
        pedidoResponsePagina.setItensPorPagina(pageable.getPageSize());
        pedidoResponsePagina.setTotalItens(pageEntity.getTotalElements());
        pedidoResponsePagina.setUltimaPagina(true);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findAllByClienteIdAndStatusPedido(usersEntity.getId(), StatusPedido.AGUARDANDO_PAGAMENTO, pageable)).thenReturn(pageEntity);
        when(pedidoMapper.toPedidoResponse(pedidoEntity)).thenReturn(pedidoResponse);
        when(paginaMapper.toPagina(pageResponse)).thenReturn(pedidoResponsePagina);

        Pagina<PedidoResponse> pedidoResponsePaginaCompare = clienteService.listarPedidos(pageable, statusPedido, errors);

        assertThat(pedidoResponsePaginaCompare.getConteudo().getFirst().getPedidoId()).isEqualTo(pedidoResponseList.getFirst().getPedidoId());
        assertThat(pedidoResponsePaginaCompare.getConteudo().getFirst().getCliente()).isEqualTo(pedidoResponseList.getFirst().getCliente());
        assertThat(pedidoResponsePaginaCompare.getConteudo().getFirst().getDataPedido()).isEqualTo(pedidoResponseList.getFirst().getDataPedido());
        assertThat(pedidoResponsePaginaCompare.getConteudo().getFirst().getValorPedido()).isEqualTo(pedidoResponseList.getFirst().getValorPedido());
        assertThat(pedidoResponsePaginaCompare.getConteudo().getFirst().getStatusPedido()).isEqualTo(pedidoResponseList.getFirst().getStatusPedido());

    }

    @DisplayName("Deve retornar InvalidStatusPedidoException poas falha ao listar pedidos")
    @Test
    public void testListarPedidosError() {
        UsersEntity usersEntity = Fixture.createUsersEntity(2L, "marcos", "marcos@gmail.com", "+0000000-0000", "senha", Role.CLIENTE, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 1);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar listar pedidos";
        String statusPedidoError = "statusError";
        errors.put("Status", "O Status digitado não existe");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new InvalidStatusPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, InvalidStatusPedidoException.class, errors);

        InvalidStatusPedidoException invalidStatusPedidoException = assertThrows(InvalidStatusPedidoException.class, () -> clienteService.listarPedidos(pageable, statusPedidoError, errors));


        assertThat(invalidStatusPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(invalidStatusPedidoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
    }

    @DisplayName("Deve retornar pedido com suceso")
    @Test
    public void testBuscarPedidoSucesso() {
        Long pedidoId = 57L;
        UsersEntity usersEntity = Fixture.createUsersEntity(2L, "anderson", "anderson@gmail.com", "+0000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(1L, LocalDateTime.now(), BigDecimal.valueOf(24), StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), null);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoEntity.getId(), usersEntity.getName(), LocalDateTime.now(), pedidoEntity.getValorPedido(), pedidoEntity.getStatusPedido().toString(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findByIdAndClienteId(pedidoId, usersEntity.getId())).thenReturn(Optional.of(pedidoEntity));
        when(pedidoMapper.toPedidoResponse(pedidoEntity)).thenReturn(pedidoResponse);

        PedidoResponse pedidoResponseCompare = clienteService.buscarPedido(pedidoId, errors);

        assertThat(pedidoResponseCompare.getPedidoId()).isEqualTo(pedidoResponse.getPedidoId());
        assertThat(pedidoResponseCompare.getCliente()).isEqualTo(pedidoResponse.getCliente());
        assertThat(pedidoResponseCompare.getDataPedido()).isEqualTo(pedidoResponse.getDataPedido());
        assertThat(pedidoResponseCompare.getValorPedido()).isEqualTo(pedidoResponse.getValorPedido());
        assertThat(pedidoResponseCompare.getStatusPedido()).isEqualTo(pedidoResponse.getStatusPedido());
        assertThat(pedidoResponseCompare.getItensPedidos()).isEqualTo(pedidoResponse.getItensPedidos());
    }

    @DisplayName("Deve retornar PedidoNotFoundException apos falha de busca do pedido")
    @Test
    public void testBuscarPedidoError() {
        Long pedidoIdError = -20L;
        UsersEntity usersEntity = Fixture.createUsersEntity(2L, "anderson", "anderson@gmail.com", "+0000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null);
        String errorMessage = usersEntity.getName() + " houve um erro ao tentar buscar pedido";
        errors.put(usersEntity.getName(), "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new PedidoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, PedidoNotFoundException.class, errors);

        PedidoNotFoundException pedidoNotFoundException = assertThrows(PedidoNotFoundException.class, () -> clienteService.buscarPedido(pedidoIdError, errors));

        assertThat(pedidoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(pedidoNotFoundException.getFields().get(usersEntity.getName())).isEqualTo(errors.get(usersEntity.getName()));
    }

    @DisplayName("Deve retornar item pedido com sucesso")
    @Test
    public void testBuscarItemPedidoSucesso() {
        Long itemPedidoId = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "carmen", "carmen@gmail.com", "+0000000-0000", "senha", Role.CLIENTE, null, new ClienteEntity(), null, null);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "carrinho", "produto de carrinho", BigDecimal.valueOf(7), 7, null, null, 2, null);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(itemPedidoId, 3, BigDecimal.valueOf(34), null, null, StatusItemPedido.PENDENTE);
        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoId, produtoEntity.getId(), produtoEntity.getNome(), 3, itemPedidoEntity.getValorTotal(), itemPedidoEntity.getStatusItemPedido());

        produtoEntity.setItensPedidos(List.of(itemPedidoEntity));
        itemPedidoEntity.setProduto(produtoEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoRepository.findByIdAndPedidoClienteId(itemPedidoId
        , usersEntity.getId())).thenReturn(Optional.of(itemPedidoEntity));
        when(itemPedidoMapper.toItemPedidoResponse(itemPedidoEntity)).thenReturn(itemPedidoResponse);

        ItemPedidoResponse itemPedidoResponseCompare = clienteService.buscarItemPedido(itemPedidoId, errors);

        assertThat(itemPedidoResponseCompare.getItemPedidoId()).isEqualTo(itemPedidoResponse.getItemPedidoId());
        assertThat(itemPedidoResponseCompare.getProdutoId()).isEqualTo(itemPedidoResponse.getProdutoId());
        assertThat(itemPedidoResponseCompare.getProduto()).isEqualTo(itemPedidoResponse.getProduto());
        assertThat(itemPedidoResponseCompare.getQuantidade()).isEqualTo(itemPedidoResponse.getQuantidade());
        assertThat(itemPedidoResponseCompare.getValorTotal()).isEqualTo(itemPedidoResponse.getValorTotal());
        assertThat(itemPedidoResponseCompare.getStatusItemPedido()).isEqualTo(itemPedidoResponse.getStatusItemPedido());
    }

    @DisplayName("Deve retornar ItemPedidoNotFoundException apos falha de busca do item pedido")
    @Test
    public void testBuscarItemPedidoError() {
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "joabe", "joabe@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        Long itemPedidoIdError = -3L;
        String errorMessage = usersEntity.getName() + " houve um erro ao tentar buscar item pedido";
        errors.put("Item pedido", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ItemPedidoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ItemPedidoNotFoundException.class, errors);

        ItemPedidoNotFoundException itemPedidoNotFoundException = assertThrows(ItemPedidoNotFoundException.class, () -> clienteService.buscarItemPedido(itemPedidoIdError, errors));

        assertThat(itemPedidoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(itemPedidoNotFoundException.getFields().get("Item pedido")).isEqualTo(errors.get("Item pedido"));
    }

    @DisplayName("Deve pagar pedido com sucesso")
    @Test
    public void testPagarPedidoSucesso() {
        Long pedidoId = 4L;
        BigDecimal valorPedido = BigDecimal.valueOf(24);

        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "carlos", "carlos@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(1L, "oculos", "produto de oculos", valorPedido, 5, null, null, 4, null);
        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(pedidoId, LocalDateTime.now(), valorPedido, StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), null);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(4L, 3, valorPedido, pedidoEntity, produtoEntity, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        produtoEntity.setItensPedidos(itemPedidoEntityList);
        pedidoEntity.setItensPedidos(itemPedidoEntityList);

        String messageSucesso = usersEntity.getName() + " seu pedido foi pago com sucesso";

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoEntity));

        String messageSucessoCompare = clienteService.pagarPedido(pedidoId, valorPedido, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar PagarPedidoException apos falha de pagamento do pedido")
    @Test
    public void testPagarPedidoError() {
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "carlos", "carlos@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        PedidoEntity pedidoEntity = mock(PedidoEntity.class);

        Long pedidoIdError = 2303L;
        BigDecimal valorPedidoError = BigDecimal.valueOf(-234);
        String errorMessage = usersEntity.getName() + " houve um erro ao tentar realizar pagamento de pedido";
        errors.put(usersEntity.getName(), "Item não encontrado");
        errors.put("Valor", "O valor fornecido é invalido para a compra do pedido");
        errors.put("Status", "O pedido já foi pago");

        doThrow(new PagarPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, PagarPedidoException.class, errors);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findById(pedidoIdError)).thenReturn(Optional.of(pedidoEntity));

        PagarPedidoException pagarPedidoException = assertThrows(PagarPedidoException.class, () -> clienteService.pagarPedido(pedidoIdError, valorPedidoError, errors));

        assertThat(pagarPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(pagarPedidoException.getFields().get(usersEntity.getName())).isEqualTo(errors.get(usersEntity.getName()));
        assertThat(pagarPedidoException.getFields().get("Valor")).isEqualTo(errors.get("Valor"));
        assertThat(pagarPedidoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
    }

    @DisplayName("Deve editar pedido com sucesso")
    @Test
    public void testEditarPedidoSucesso() {
        Long pedidoId = 2L;
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "magno", "magno@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(1L, 1);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(23), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(pedidoId, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), itemPedidoEntityList);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(itemPedidoRequest.getProdutoId(), "tenis", "produto de tenis", itemPedidoEntity.getValorTotal(), 24, null, itemPedidoEntityList, 2, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoEntity.getId(), itemPedidoRequest.getProdutoId(), produtoEntity.getNome(), itemPedidoRequest.getQuantidade(), itemPedidoEntity.getValorTotal(), itemPedidoEntity.getStatusItemPedido());
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        itemPedidoEntity.setPedido(pedidoEntity);
        itemPedidoEntity.setProduto(produtoEntity);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoId, usersEntity.getName(), pedidoEntity.getDataPedido(), pedidoEntity.getValorPedido(), pedidoEntity.getStatusPedido().toString(), itemPedidoResponseList);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoEntity));
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);
        when(produtoRepository.findAllById(any(List.class))).thenReturn(produtoEntityList);
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);
        when(pedidoMapper.toPedidoResponse(pedidoEntity)).thenReturn(pedidoResponse);

        PedidoResponse pedidoResponseCompare = clienteService.editarPedido(pedidoId, itemPedidoRequestList, errors);

        assertThat(pedidoResponseCompare.getPedidoId()).isEqualTo(pedidoResponse.getPedidoId());
        assertThat(pedidoResponseCompare.getCliente()).isEqualTo(pedidoResponse.getCliente());
        assertThat(pedidoResponseCompare.getDataPedido()).isEqualTo(pedidoResponse.getDataPedido());
        assertThat(pedidoResponseCompare.getValorPedido()).isEqualTo(pedidoResponse.getValorPedido());
        assertThat(pedidoResponseCompare.getStatusPedido()).isEqualTo(pedidoResponse.getStatusPedido());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getItemPedidoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getItemPedidoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProdutoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProdutoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProduto()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProduto());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getQuantidade()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getQuantidade());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getValorTotal()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getValorTotal());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getStatusItemPedido()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getStatusItemPedido());
    }

    @DisplayName("Deve retornar EditarPedidoException apos falha de edição do pedido")
    @Test
    public void testEditarPedidoError() {
        Long pedidoIdError = 234L;

        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "magno", "magno@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(1L, 1);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, itemPedidoRequest.getQuantidade(), BigDecimal.valueOf(12), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(itemPedidoRequest.getProdutoId(), "lixeira", "produto de lixeira", itemPedidoEntity.getValorTotal(), 4, null, itemPedidoEntityList, 2, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);


        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(pedidoIdError, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), itemPedidoEntityList);

        itemPedidoEntity.setProduto(produtoEntity);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar atualizar seu pedido";
        errors.put(usersEntity.getName(), "Item não encontrado");
        errors.put("Status", "Só é possível editar um pedido que ainda não foi pago");
        errors.put(produtoEntity.getNome(), "Quantidade acima do estoque ou igual a 0 para este produto");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);
        when(produtoRepository.findAllById(any(Iterable.class)))
         .thenReturn(produtoEntityList);
        when(pedidoRepository.findById(pedidoIdError)).thenReturn(Optional.of(pedidoEntity));
        doThrow(new EditarPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, EditarPedidoException.class, errors);

        EditarPedidoException editarPedidoException = assertThrows(EditarPedidoException.class, () -> clienteService.editarPedido(pedidoIdError, itemPedidoRequestList, errors));

        assertThat(editarPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(editarPedidoException.getFields().get(usersEntity.getName())).isEqualTo(errors.get(usersEntity.getName()));
        assertThat(editarPedidoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
        assertThat(editarPedidoException.getFields().get(produtoEntity.getNome())).isEqualTo(errors.get(produtoEntity.getNome()));
    }

    @DisplayName("Deve adicionar produto ao pedido com sucesso")
    @Test
    public void testAdicionarProdutoAPedidoSucesso() {
        Long pedidoId = 30L;
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "felipe", "felipe@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(2L, 2);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, 2, BigDecimal.valueOf(23), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(1L, LocalDateTime.now(), BigDecimal.valueOf(23), StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), new ArrayList<>());
        List<PedidoEntity> pedidoEntityList = List.of(pedidoEntity);

        ProdutoEntity produtoEntity = Fixture.createProdutoEntity(itemPedidoRequest.getProdutoId(), "notebook", "produto de notebook", itemPedidoEntity.getValorTotal(), 3, null, itemPedidoEntityList, 2, null);
        List<ProdutoEntity> produtoEntityList = List.of(produtoEntity);

        ItemPedidoResponse itemPedidoResponse = Fixture.createItemPedidoResponse(itemPedidoEntity.getId(), itemPedidoRequest.getProdutoId(), produtoEntity.getNome(), itemPedidoRequest.getQuantidade(), itemPedidoEntity.getValorTotal(), itemPedidoEntity.getStatusItemPedido());
        List<ItemPedidoResponse> itemPedidoResponseList = List.of(itemPedidoResponse);

        PedidoResponse pedidoResponse = Fixture.createPedidoResponse(pedidoEntity.getId(), usersEntity.getName(), pedidoEntity.getDataPedido(), pedidoEntity.getValorPedido(), pedidoEntity.getStatusPedido().toString(), itemPedidoResponseList);

        itemPedidoEntity.setPedido(new PedidoEntity());
        itemPedidoEntity.setProduto(produtoEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoEntity));
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);
        when(produtoRepository.findAllById(any(Iterable.class))).thenReturn(produtoEntityList);
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);
        when(pedidoMapper.toPedidoResponse(pedidoEntity)).thenReturn(pedidoResponse);

     PedidoResponse pedidoResponseCompare = clienteService.adicionarProdutoAPedido(pedidoId, itemPedidoRequestList, errors);

        assertThat(pedidoResponseCompare.getPedidoId()).isEqualTo(pedidoResponse.getPedidoId());
        assertThat(pedidoResponseCompare.getCliente()).isEqualTo(pedidoResponse.getCliente());
        assertThat(pedidoResponseCompare.getDataPedido()).isEqualTo(pedidoResponse.getDataPedido());
        assertThat(pedidoResponseCompare.getValorPedido()).isEqualTo(pedidoResponse.getValorPedido());
        assertThat(pedidoResponseCompare.getStatusPedido()).isEqualTo(pedidoResponse.getStatusPedido());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getItemPedidoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getItemPedidoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProdutoId()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProdutoId());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getProduto()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getProduto());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getQuantidade()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getQuantidade());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getValorTotal()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getValorTotal());
        assertThat(pedidoResponseCompare.getItensPedidos().getFirst().getStatusItemPedido()).isEqualTo(pedidoResponse.getItensPedidos().getFirst().getStatusItemPedido());
    }

    @DisplayName("Deve retornar AdicionarProdutoAPedidoException apos falha de adicionar produto ao pedido")
    @Test
    public void testAdicionarProdutoAPedidoError() {
        Long pedidoIdError = 023L;
        UsersEntity usersEntity = Fixture.createUsersEntity(4L, "felipe", "felipe@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        ItemPedidoRequest itemPedidoRequest = Fixture.createItemPedidoRequest(2L, 2);
        List<ItemPedidoRequest> itemPedidoRequestList = List.of(itemPedidoRequest);

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, 2, BigDecimal.valueOf(23), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = List.of(itemPedidoEntity);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar adicionar produto a um pedido";
        errors.put(usersEntity.getName(), "Item não encontrado");
        errors.put("Status", "Status do pedido inválido para adicionar produto");
        errors.put("Produto", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequestList)).thenReturn(itemPedidoEntityList);

        doThrow(new AdicionarProdutoAPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, AdicionarProdutoAPedidoException.class, errors);

        AdicionarProdutoAPedidoException adicionarProdutoAPedidoException = assertThrows(AdicionarProdutoAPedidoException.class, () -> clienteService.adicionarProdutoAPedido(pedidoIdError, itemPedidoRequestList, errors));

        assertThat(adicionarProdutoAPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(adicionarProdutoAPedidoException.getFields().get(usersEntity.getName())).isEqualTo(errors.get(usersEntity.getName()));
        assertThat(adicionarProdutoAPedidoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
        assertThat(adicionarProdutoAPedidoException.getFields().get("Produto")).isEqualTo(errors.get("Produto"));
    }

    @DisplayName("Deve deletar item pedido com sucesso")
    @Test
    public void testDeletarItemPedidoSucesso() {
        Long itemPedidoId = 3L;

        UsersEntity usersEntity = Fixture.createUsersEntity(40L, "raimundo", "raimundo@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        String messageSucesso = usersEntity.getName() + " seu item pedido foi deletado com sucesso";

        ItemPedidoEntity itemPedidoEntity = Fixture.createItemPedidoEntity(1L, 4, BigDecimal.valueOf(43), null, null, StatusItemPedido.PENDENTE);
        List<ItemPedidoEntity> itemPedidoEntityList = new ArrayList<>();
        itemPedidoEntityList.add(itemPedidoEntity);

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(2L, LocalDateTime.now(), itemPedidoEntity.getValorTotal(), StatusPedido.AGUARDANDO_PAGAMENTO, usersEntity.getCliente(), itemPedidoEntityList);

        itemPedidoEntity.setPedido(pedidoEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(itemPedidoRepository.findById(itemPedidoId)).thenReturn(Optional.of(itemPedidoEntity));

        clienteService.deletarItemPedido(itemPedidoId, errors);
    }

    @DisplayName("Deve retornar DeletarItemPedidoException apos falha de deletar item pedido")
    @Test
    public void testDeletarItemPedidoError() {
        Long itemPedidoIdError = 233L;

        UsersEntity usersEntity = Fixture.createUsersEntity(40L, "raimundo", "raimundo@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar buscar pelo item pedido";
        errors.put("Item pedido", "Item não encontrado");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new ItemPedidoNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, ItemPedidoNotFoundException.class, errors);

        ItemPedidoNotFoundException itemPedidoNotFoundException = assertThrows(ItemPedidoNotFoundException.class, () -> clienteService.deletarItemPedido(itemPedidoIdError, errors));

        assertThat(itemPedidoNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(itemPedidoNotFoundException.getFields().get("Item pedido")).isEqualTo(errors.get("Item pedido"));
    }

    @DisplayName("Deve deletar pedido com sucesso")
    @Test
    public void testDeletarPedidoSucesso() {
        Long pedidoId = 32L;

        UsersEntity usersEntity = Fixture.createUsersEntity(3L, "joao", "joao@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        String messageSucesso = usersEntity.getName() + " seu pedido foi deletado com sucesso";

        PedidoEntity pedidoEntity = Fixture.createPedidoEntity(2L, LocalDateTime.now(), BigDecimal.valueOf(256), StatusPedido.PAGO, usersEntity.getCliente(), null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoEntity));

        String messageSucessoCompare = clienteService.deletarPedido(pedidoId, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar DeletarPedidoException apos falha de deletar pedido")
    @Test
    public void testDeletarPedidoError() {
        Long pedidoIdError = -234L;

        UsersEntity usersEntity = Fixture.createUsersEntity(3L, "joao", "joao@gmail.com", "+0000000-0000", "senha",  Role.CLIENTE, null, new ClienteEntity(), null, null);

        String errorMessage = usersEntity.getName() + " houve um erro ao tentar deletar seu pedido";
        errors.put(usersEntity.getName(), "Item não encontrado");
        errors.put("Status", "Status do pedido inválido para deletar item pedido");

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new DeletarPedidoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, DeletarPedidoException.class, errors);

        DeletarPedidoException deletarPedidoException = assertThrows(DeletarPedidoException.class, () -> clienteService.deletarPedido(pedidoIdError, errors));

        assertThat(deletarPedidoException.getMessage()).isEqualTo(errorMessage);
        assertThat(deletarPedidoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
        assertThat(deletarPedidoException.getFields().get(usersEntity.getName())).isEqualTo(errors.get(usersEntity.getName()));
    }
}
