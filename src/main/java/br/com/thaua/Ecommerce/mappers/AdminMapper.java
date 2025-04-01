package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminResponse adminEntityToAdminResponse(AdminEntity adminEntity);
    List<AdminResponse> adminEntityToAdminResponseList(List<AdminEntity> adminEntityList);
}
