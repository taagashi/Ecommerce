package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.mappers.ItemPedidoMapper;
import br.com.thaua.Ecommerce.mappers.PedidoMapper;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import br.com.thaua.Ecommerce.repositories.PedidoRepository;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ClienteResponse atualizarCpfETelefone(ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.getCliente().setCpf(clienteCpfTelefoneRequest.getCpf());
        usersEntity.getCliente().setTelefone(clienteCpfTelefoneRequest.getTelefone());

        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }

    public ClienteComPedidoResponse exibirPerfil() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        return clienteMapper.toResponseComPedido(usersEntity.getCliente());
    }


    public ClienteResponse atualizarDados(ClienteUpdateRequest clienteUpdateRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.setName(clienteUpdateRequest.getName());
        usersEntity.setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setName(clienteUpdateRequest.getName());
        usersEntity.getCliente().setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setCpf(clienteUpdateRequest.getCpf());
        usersEntity.getCliente().setTelefone(clienteUpdateRequest.getTelefone());

        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }

    public PedidoResponse fazerPedido(List<ItemPedidoRequest> itemPedidoRequest) {
       UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        List<ItemPedidoEntity> itemPedidoEntityList = itemPedidoMapper.toItemPedidoEntityList(itemPedidoRequest);

//        coletando todos os ids dos produtos
        List<Long> produtosIds = itemPedidoRequest.stream().map(ItemPedidoRequest::getProdutoId).toList();

//        buscando todos os produtos de uma so vez
        Map<Long, ProdutoEntity> produtoEntityMap = produtoRepository.findAllById(produtosIds).stream().collect(Collectors.toMap(produto -> produto.getId(), produto -> produto));

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setValorPedido(BigDecimal.ZERO);
        pedidoEntity.setDataPedido(LocalDateTime.now());
        pedidoEntity.setCliente(usersEntity.getCliente());
        pedidoEntity.setStatusPedido(StatusPedido.ENVIADO);

        for(int i=0 ; i<itemPedidoEntityList.size() ; i++) {
            Long produtoId = produtosIds.get(i);
            ProdutoEntity produtoEntity = produtoEntityMap.get(produtoId);
            itemPedidoEntityList.get(i).setProduto(produtoEntity);
            itemPedidoEntityList.get(i).setValorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemPedidoEntityList.get(i).getQuantidade())));
            itemPedidoEntityList.get(i).setPedido(pedidoEntity);
            pedidoEntity.setValorPedido(pedidoEntity.getValorPedido().add(itemPedidoEntityList.get(i).getValorTotal()));

        }

        pedidoEntity.setItensPedidos(itemPedidoEntityList);
        pedidoRepository.save(pedidoEntity);
        return pedidoMapper.toPedidoResponse(pedidoEntity);
    }
}
