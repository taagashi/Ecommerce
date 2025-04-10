package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "telefone", source = "users.telefone")
    ClienteResponse toResponse(ClienteEntity clienteEntity);
    List<ClienteResponse> toResponse(List<ClienteEntity> clienteEntities);

    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "telefone", source = "users.telefone")
    @Mapping(target = "pedidosFeitos", expression = "java(clienteEntity.getPedido() == null ? 0 : clienteEntity.getPedido().size())")
    ClienteComPedidoResponse toResponseComPedido(ClienteEntity clienteEntity);
}
