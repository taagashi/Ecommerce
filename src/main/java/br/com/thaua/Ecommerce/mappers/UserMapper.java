package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.dto.users.UsersResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UsersEntity toEntity(UsersRequest usersRequest);
    UsersResponse toResponse(UsersEntity usersEntity);
}
