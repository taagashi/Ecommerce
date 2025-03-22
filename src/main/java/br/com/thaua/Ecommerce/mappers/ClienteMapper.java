package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.dto.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", expression = "java(clienteEntity.getUsers().getId())")
    ClienteResponse toResponse(ClienteEntity clienteEntity);
    List<ClienteResponse> toResponse(List<ClienteEntity> clienteEntities);
}
