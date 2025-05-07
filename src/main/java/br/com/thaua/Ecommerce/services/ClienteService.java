package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import br.com.thaua.Ecommerce.domain.entity.*;
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
    private final ExtractTypeUserContextHolder extractTypeUserContextHolder;

    @Transactional
    public ClienteResponse atualizarCpfETelefone(ClienteCpfTelefoneRequest clienteCpfTelefoneRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        usersEntity.getCliente().setCpf(clienteCpfTelefoneRequest.getCpf());
        usersEntity.setTelefone(clienteCpfTelefoneRequest.getTelefone());


        log.info("SERVICE CLIENTE - ATUALIZAR CPF E TELEFONE");
        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }

    @Transactional
    public String atualizarDados(ClienteUpdateRequest clienteUpdateRequest) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        usersEntity.setName(clienteUpdateRequest.getName());
        usersEntity.setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setCpf(clienteUpdateRequest.getCpf());
        usersEntity.setTelefone(clienteUpdateRequest.getTelefone());

        usersRepository.save(usersEntity);

        log.info("SERVICE CLIENTE - ATUALIZAR DADOS ");
        return jwtService.generateToken(new MyUserDetails(usersEntity.getId(), usersEntity.getEmail(), usersEntity.getRole().toString(), usersEntity.getPassword(), usersEntity));
    }

    @Transactional
    public PedidoResponse fazerPedido(List<ItemPedidoRequest> itemPedidoRequest, Map<String, String> errors) {
       UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.validarTelefone(usersEntity, errors);

        List<ItemPedidoEntity> itemPedidoEntityList = itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequest);
        List<Long> produtosIds = itemPedidoRequest.stream().map(ItemPedidoRequest::getProdutoId).toList();
        Map<Long, ProdutoEntity> produtoEntityMap = produtoRepository.findAllById(produtosIds).stream().collect(Collectors.toMap(AbstractEntity::getId, produto -> produto));

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setValorPedido(BigDecimal.ZERO);
        pedidoEntity.setDataPedido(LocalDateTime.now());
        pedidoEntity.setCliente(usersEntity.getCliente());

        for(int i=0 ; i<itemPedidoEntityList.size() ; i++) {
            Long produtoId = produtosIds.get(i);
            ProdutoEntity produtoEntity = produtoEntityMap.get(produtoId);

            validationService.validarQuantidadePedido(itemPedidoEntityList.get(i), produtoEntity, errors);

            itemPedidoEntityList.get(i).setProduto(produtoEntity);
            itemPedidoEntityList.get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
            itemPedidoEntityList.get(i).setPedido(pedidoEntity);
            pedidoEntity.setValorPedido(pedidoEntity.getValorPedido().add(itemPedidoEntityList.get(i).getValorTotal()));
        }

        validationService.analisarException(usersEntity.getName() + ", houve um erro na hora de fazer um pedido", FazerPedidoException.class, errors);

        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        pedidoEntity.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);

        log.info("SERVICE CLIENTE - FAZER PEDIDO");
        pedidoRepository.save(pedidoEntity);

        return pedidoMapper.toPedidoResponse(pedidoEntity);
    }

    public Pagina<PedidoResponse> listarPedidos(Pageable pageable, String statusPedido, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        if(statusPedido != null) {
            log.info("SERVICE CLIENTE - LISTAR PEDIDOS COM FILTRO");
            validationService.validarStatusPedidoListagem(statusPedido, errors);
            validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar listar pedidos", InvalidStatusPedidoException.class, errors);
            return paginaMapper.toPagina(pedidoRepository.findAllByClienteIdAndStatusPedido(usersEntity.getId(), StatusPedido.valueOf(statusPedido), pageable).map(pedidoMapper::toPedidoResponse));
        }

        log.info("SERVICE CLIENTE - LISTAR PEDIDOS");
        return paginaMapper.toPagina(pedidoRepository.findAllByClienteId(usersEntity.getId(), pageable).map(pedidoMapper::toPedidoResponse));
    }

    public PedidoResponse buscarPedido(Long pedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findByIdAndClienteId(pedidoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar buscar pedido", PedidoNotFoundException.class, errors);


        log.info("SERVICE CLIENTE - BUSCAR PEDIDO");
        return pedidoMapper.toPedidoResponse(pedidoEntity.get());
    }

    public ItemPedidoResponse buscarItemPedido(Long itemPedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ItemPedidoEntity> itemPedidoEntity = itemPedidoRepository.findByIdAndPedidoClienteId(itemPedidoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(itemPedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar buscar item pedido", ItemPedidoNotFoundException.class, errors);


        log.info("SERVICE CLIENTE - BUSCAR ITEM PEDIDO");
        return itemPedidoMapper.toItemPedidoResponse(itemPedidoEntity.get());
    }

    @Transactional
    public String pagarPedido(Long pedidoId, BigDecimal valorPedido, Map<String, String> errors){
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.validarPagamentoPedido(pedidoEntity.get(), valorPedido, errors);
        validationService.validarStatusPedidoPagamento(pedidoEntity.get(), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar realizar pagamento de pedido", PagarPedidoException.class, errors);

        for(ItemPedidoEntity itemPedido : pedidoEntity.get().getItensPedidos()) {
            itemPedido.getProduto().setEstoque(itemPedido.getProduto().getEstoque() - itemPedido.getQuantidade());
            itemPedido.getProduto().setQuantidadeDemanda(itemPedido.getProduto().getQuantidadeDemanda() + 1);
            itemPedido.setStatusItemPedido(StatusItemPedido.PROCESSANDO);
            itemPedidoRepository.save(itemPedido);
        }

        pedidoEntity.get().setStatusPedido(StatusPedido.PAGO);

        log.info("SERVICE CLIENTE - PAGAR PEDIDO");
        return usersEntity.getName() + " seu pedido foi pago com sucesso";
    }

    public PedidoResponse editarPedido(Long pedidoId, List<ItemPedidoRequest> itemPedidoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.validarStatusPedidoEditar(pedidoEntity.orElse(null), errors);

        List<ItemPedidoEntity> itemPedidoEntityList = itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequest);
        List<Long> produtosIds = itemPedidoRequest.stream().map(ItemPedidoRequest::getProdutoId).toList();
        Map<Long, ProdutoEntity> produtoEntityMap = produtoRepository.findAllById(produtosIds).stream().collect(Collectors.toMap(AbstractEntity::getId, produto -> produto));

        pedidoEntity.get().setValorPedido(BigDecimal.valueOf(0));

        for(int i=0 ; i<itemPedidoEntityList.size() ; i++) {
            Long idProduto = produtosIds.get(i);
            ProdutoEntity produtoEntity = produtoEntityMap.get(idProduto);

            validationService.validarQuantidadePedido(itemPedidoEntityList.get(i), produtoEntity, errors);

            if(i <= pedidoEntity.get().getItensPedidos().size()-1) {
                pedidoEntity.get().getItensPedidos().get(i).setProduto(produtoEntity);
                pedidoEntity.get().getItensPedidos().get(i).setPedido(pedidoEntity.get());
                pedidoEntity.get().getItensPedidos().get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
                pedidoEntity.get().getItensPedidos().get(i).setQuantidade(itemPedidoEntityList.get(i).getQuantidade());
                pedidoEntity.get().setValorPedido(pedidoEntity.get().getValorPedido().add(pedidoEntity.get().getItensPedidos().get(i).getValorTotal()));
            } else {
                itemPedidoEntityList.get(i).setProduto(produtoEntity);
                itemPedidoEntityList.get(i).setPedido(pedidoEntity.get());
                itemPedidoEntityList.get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
                pedidoEntity.get().getItensPedidos().add(itemPedidoEntityList.get(i));
                pedidoEntity.get().setValorPedido(pedidoEntity.get().getValorPedido().add(itemPedidoEntityList.get(i).getValorTotal()));
            }

        }

        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar atualizar seu pedido", EditarPedidoException.class, errors);

        log.info("SERVICE CLIENTE - EDITAR PEDIDO");
        return pedidoMapper.toPedidoResponse(pedidoRepository.save(pedidoEntity.get()));
    }

    public PedidoResponse adicionarProdutoAPedido(Long pedidoId, List<ItemPedidoRequest> itemPedidoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.validarStatusPedidoAdicionarProduto(pedidoEntity.orElse(null), errors);

        List<ItemPedidoEntity> itemPedidoEntityList = itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequest);
        List<Long> produtosIds = itemPedidoRequest.stream().map(ItemPedidoRequest::getProdutoId).toList();
        Map<Long, ProdutoEntity> produtoEntityMap = produtoRepository.findAllById(produtosIds).stream().collect(Collectors.toMap(AbstractEntity::getId, produto -> produto));

        validationService.validarExistenciaEntidade(produtoEntityMap, errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar adicionar produto a um pedido", AdicionarProdutoAPedidoException.class, errors);

        for(int i=0 ; i<itemPedidoEntityList.size() ; i++) {
            Long produtoId = produtosIds.get(i);
            ProdutoEntity produtoEntity =  produtoEntityMap.get(produtoId);
            itemPedidoEntityList.get(i).setPedido(pedidoEntity.get());
            itemPedidoEntityList.get(i).setProduto(produtoEntity);
            itemPedidoEntityList.get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
            pedidoEntity.get().setValorPedido(pedidoEntity.get().getValorPedido().add(itemPedidoEntityList.get(i).getValorTotal()));
            pedidoEntity.get().getItensPedidos().add(itemPedidoEntityList.get(i));
        }

        log.info("SERVICE CLIENTE - ADICIOIANR PRODUTO A  PEDIDO");
        return pedidoMapper.toPedidoResponse(pedidoRepository.save(pedidoEntity.get()));
    }

    public String deletarItemPedido(Long itemPedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<ItemPedidoEntity> itemPedidoEntity = itemPedidoRepository.findById(itemPedidoId);

        validationService.validarExistenciaEntidade(itemPedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar buscar pelo item pedido", ItemPedidoNotFoundException.class, errors);

        PedidoEntity pedidoEntity = itemPedidoEntity.get().getPedido();

        validationService.validarStatusPedidoDeletar(pedidoEntity, errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar deletar seu item pedido", InvalidStatusPedidoException.class, errors);

        pedidoEntity.getItensPedidos().remove(itemPedidoEntity.get());

        pedidoRepository.save(pedidoEntity);
        itemPedidoRepository.delete(itemPedidoEntity.get());

        log.info("SERVICE CLIENTE - DELETAR ITEM PEDIDO");
        return usersEntity.getName() + " seu item pedido foi deletado com sucesso";
    }

    public String deletarPedido(Long pedidoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.validarStatusPedidoDeletar(pedidoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar deletar seu pedido", DeletarPedidoException.class, errors);

        pedidoRepository.delete(pedidoEntity.get());

        log.info("SERVICE CLIENTE - DELETAR PEDIDO");
        return usersEntity.getName() + " seu pedido foi deletado com sucesso";
    }
}
