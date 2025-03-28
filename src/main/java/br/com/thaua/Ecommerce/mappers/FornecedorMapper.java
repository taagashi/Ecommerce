package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {
    FornecedorResponse FornecedorToResponse(FornecedorEntity fornecedorEntity);
    List<FornecedorResponse> toFornecedorResponseListResponse(List<FornecedorEntity> fornecedorEntityList);
}
