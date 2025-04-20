package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.exceptions.ClienteException;
import br.com.thaua.Ecommerce.exceptions.ItemPedidoNotFoundException;
import br.com.thaua.Ecommerce.exceptions.PedidoException;
import br.com.thaua.Ecommerce.exceptions.PedidoNotFoundException;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.mappers.ItemPedidoMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.PedidoMapper;
import br.com.thaua.Ecommerce.repositories.*;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UsersRepository usersRepository;
    private final ClienteMapper clienteMapper;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoMapper itemPedidoMapper;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ValidationService validationService;
    private final PaginaMapper paginaMapper;
    private final JWTService jwtService;

//    @CachePut(value = "clientes", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    @Transactional
    public ClienteResponse atualizarCpfETelefone(ClienteCpfTelefoneRequest clienteCpfTelefoneRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        usersEntity.getCliente().setCpf(clienteCpfTelefoneRequest.getCpf());
        usersEntity.setTelefone(clienteCpfTelefoneRequest.getTelefone());


        log.info("EXECUTANDO SERVICE-CLIENTE ATUALIZAR CPF E TELEFONE");
        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }

//    @Cacheable(value = "clientes",key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    public ClienteComPedidoResponse exibirPerfil() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        log.info("EXECUTANDO SERVICE-CLIENTE EXIBIR PERFIL");
        return clienteMapper.toResponseComPedido(usersEntity.getCliente());
    }


//    @CachePut(value = "clientes", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    @Transactional
    public String atualizarDados(ClienteUpdateRequest clienteUpdateRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.setName(clienteUpdateRequest.getName());
        usersEntity.setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setCpf(clienteUpdateRequest.getCpf());
        usersEntity.setTelefone(clienteUpdateRequest.getTelefone());

        usersRepository.save(usersEntity);

        log.info("EXECUTANDO SERVICE-CLIENTE ATUALIZAR DADOS");
        return jwtService.generateToken(new MyUserDetails(usersEntity.getId(), usersEntity.getEmail(), usersEntity.getRole().toString(), usersEntity.getPassword(), usersEntity));
    }

//    @CachePut(value = "pedidos", key = "T(org.springframework.security.core.context.SecurityContexHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    @Transactional
    public PedidoResponse fazerPedido(List<ItemPedidoRequest> itemPedidoRequest, Map<String, String> errors) {
       UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.validarTelefone(usersEntity, errors);
        validationService.validarQuantidadePedido(itemPedidoRequest, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro na hora de fazer um pedido", ClienteException.class, errors);

        List<ItemPedidoEntity> itemPedidoEntityList = itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequest);

//        coletando todos os ids dos produtos
        List<Long> produtosIds = itemPedidoRequest.stream().map(ItemPedidoRequest::getProdutoId).toList();

//        buscando todos os produtos de uma so vez
        Map<Long, ProdutoEntity> produtoEntityMap = produtoRepository.findAllById(produtosIds).stream().collect(Collectors.toMap(produto -> produto.getId(), produto -> produto));

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setValorPedido(BigDecimal.ZERO);
        pedidoEntity.setDataPedido(LocalDateTime.now());
        pedidoEntity.setCliente(usersEntity.getCliente());

        for(int i=0 ; i<itemPedidoEntityList.size() ; i++) {
            Long produtoId = produtosIds.get(i);
            ProdutoEntity produtoEntity = produtoEntityMap.get(produtoId);
            itemPedidoEntityList.get(i).setProduto(produtoEntity);
            itemPedidoEntityList.get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
            itemPedidoEntityList.get(i).setPedido(pedidoEntity);
            pedidoEntity.setValorPedido(pedidoEntity.getValorPedido().add(itemPedidoEntityList.get(i).getValorTotal()));

        }

        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        pedidoEntity.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);

        log.info("EXECUTANDO SERVICE-CLIENTE FAZER PEDIDO");
        pedidoRepository.save(pedidoEntity);
            return pedidoMapper.toPedidoResponse(pedidoEntity);
    }

//    @Cacheable(value = "pedidos", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    public Pagina<PedidoResponse> listarPedidos(Pageable pageable) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();


        log.info("EXECUTANDO SERVICE-CLIENTE LISTAR PEDIDOS");
        return paginaMapper.toPagina(pedidoRepository.findAllByClienteId(usersEntity.getId(), pageable).map(pedidoMapper::toPedidoResponse));
    }

//    @Cacheable(value = "pedidos", key = "(Org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    public PedidoResponse buscarPedido(Long pedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findByIdAndClienteId(pedidoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar buscar pedido", PedidoNotFoundException.class, errors);


        log.info("EXECUTANDO SERVICE-CLIENTE BUSCAR PEDIDO");
        return pedidoMapper.toPedidoResponse(pedidoEntity.get());
    }

//    @Cacheable(value = "pedidos", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    public ItemPedidoResponse buscarItemPedido(Long itemPedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ItemPedidoEntity> itemPedidoEntity = itemPedidoRepository.findByIdAndPedidoClienteId(itemPedidoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(itemPedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar buscar item pedido", ItemPedidoNotFoundException.class, errors);


        log.info("EXECUTANDO SERVICE-CLIENTE BUSCAR ITEM PEDIDO");
        return itemPedidoMapper.toItemPedidoResponse(itemPedidoEntity.get());
    }

    @Transactional
    public String pagarPedido(Long pedidoId, BigDecimal valorPedido, Map<String, String> errors){
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.validarPagamentoPedido(pedidoEntity.get(), valorPedido, errors);
        validationService.validarStatusPedidoPagamento(pedidoEntity.get(), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar realizar pagamento de pedido", PedidoException.class, errors);

        for(ItemPedidoEntity itemPedido : pedidoEntity.get().getItensPedidos()) {
            itemPedido.getProduto().setEstoque(itemPedido.getProduto().getEstoque() - itemPedido.getQuantidade());
            itemPedido.setStatusItemPedido(StatusItemPedido.PROCESSANDO);
            itemPedidoRepository.save(itemPedido);
        }

        pedidoEntity.get().setStatusPedido(StatusPedido.PAGO);

        return usersEntity.getName() + " seu pedido foi pago com sucesso";
    }
}
