package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteResponse toResponse(ClienteEntity clienteEntity);
    List<ClienteResponse> toResponse(List<ClienteEntity> clienteEntities);
}
