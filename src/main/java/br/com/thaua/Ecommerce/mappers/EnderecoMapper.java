package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.EnderecoEntity;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoEntity enderecoRequestToEntity(EnderecoRequest enderecoRequest);
    @Mapping(target = "nameUser", expression = "java(enderecoEntity.getUsers().getName())")
    EnderecoResponse toEnderecoResponse(EnderecoEntity enderecoEntity);
}
