package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    ItemPedidoEntity toItemPedidoEntity(ItemPedidoRequest itemPedidoRequest);
    List<ItemPedidoEntity> toItemPedidoEntityList(List<ItemPedidoRequest> itemPedidoRequestList);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "produto", expression = "java(itemPedidoEntity.getProduto().getNome())")
    @Mapping(target = "itemPedidoId", source = "id")
    ItemPedidoResponse toItemPedidoResponse(ItemPedidoEntity itemPedidoEntity);
}
