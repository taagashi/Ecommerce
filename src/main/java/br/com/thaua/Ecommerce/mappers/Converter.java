package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Converter {
    UsersEntity toEntity(UsersRequest usersRequest);
    UsersResponse toResponse(UsersEntity usersEntity);
}
