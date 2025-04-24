package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorSaldoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {
    @Mapping(target = "name", source = "users.name")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "telefone", source = "users.telefone")
    FornecedorResponse FornecedorToResponse(FornecedorEntity fornecedorEntity);
    List<FornecedorResponse> toFornecedorResponseListResponse(List<FornecedorEntity> fornecedorEntityList);

    FornecedorSaldoResponse fornecedorEntityToFornecedorSaldoResponse(FornecedorEntity fornecedorEntity);
}
