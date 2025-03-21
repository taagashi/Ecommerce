package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.UsersLoginRequest;
import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Converter {
    UsersEntity toEntity(UsersRequest usersRequest);
    UsersEntity toEntity(UsersLoginRequest usersLoginRequest);

    UsersResponse toResponse(UsersEntity usersEntity);
}
