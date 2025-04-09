package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "email", source = "users.email")
    AdminResponse adminEntityToAdminResponse(AdminEntity adminEntity);
    List<AdminResponse> adminEntityToAdminResponseList(List<AdminEntity> adminEntityList);
}
