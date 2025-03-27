package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface CategoriaMapper {
    @Mapping(target = "produtosAssociados", expression = "java(categoriaEntity.getProdutos() == null ? 0 : categoriaEntity.getProdutos().size())")
    CategoriaResponse toResponse(CategoriaEntity categoriaEntity);

    CategoriaEntity toEntity(CategoriaRequest categoriaRequest);

    List<CategoriaResponse> toResponse(List<CategoriaEntity> categoriaEntityList);

    @Mapping(target = "categoriaId", source = "id")
    CategoriaProdutosResponse toCategoriaProdutosResponse(CategoriaEntity categoriaEntity);

    @Mapping(target = "produtoId", source = "id")
    ProdutoComponentResponse toProdutoComponentResponse(ProdutoEntity produtoEntity);

    List<ProdutoComponentResponse> toProdutoComponentResponseList(List<ProdutoEntity> produtoEntityList);
}
