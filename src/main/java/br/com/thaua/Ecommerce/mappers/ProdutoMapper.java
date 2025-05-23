package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.produto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    @Mapping(target = "estoque", source = "novaQuantidade")
    ProdutoEntity produtoEstoqueRequestToEntity(ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest);

    ProdutoEntity produtoRequestToEntity(ProdutoRequest produtoRequest);

    @Mapping(target = "produtoId", source = "id")
    @Mapping(target = "categoriasAssociadas", expression = "java(produtoEntity.getCategorias() == null ? 0 : produtoEntity.getCategorias().size())")
    ProdutoResponse produtoToResponse(ProdutoEntity produtoEntity);

    List<ProdutoResponse> produtoToResponseList(List<ProdutoEntity> produtoEntityList);

    List<ProdutoEntity> produtoRequestToEntityList(List<ProdutoRequest> produtoRequestList);

    @Mapping(target = "produtoId", source = "id")
    ProdutoCategoriaResponse toProdutoCategoriaResponse(ProdutoEntity produtoEntity);

    List<CategoriaComponentResponse> toCategoriaComponentResponseList(List<CategoriaEntity> categoriaEntityList);

    @Mapping(target = "categoriaId", source = "id")
    CategoriaComponentResponse toCategoriaComponentResponse(CategoriaEntity categoria);

    List<ProdutoEntity> produtoRequestToProdutoEntity(List<ProdutoRequest> produtoRequest);
}
