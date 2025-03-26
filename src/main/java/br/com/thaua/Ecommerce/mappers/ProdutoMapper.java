package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoEntity produtoRequestToEntity(ProdutoRequest produtoRequest);
    @Mapping(target = "produtoId", source = "id")
    @Mapping(target = "quantidadePedidos", expression = "java(produtoEntity.getItensPedidos() == null ? 0 : produtoEntity.getItensPedidos().size())")
    @Mapping(target = "numeroCategoria", expression = "java(produtoEntity.getCategorias() == null ? 0 : produtoEntity.getCategorias().size())")
    ProdutoResponse produtoToResponse(ProdutoEntity produtoEntity);

    List<ProdutoResponse> produtoToResponseList(List<ProdutoEntity> produtoEntityList);
}
