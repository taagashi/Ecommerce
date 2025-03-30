package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    ItemPedidoEntity toItemPedidoEntity(ItemPedidoRequest itemPedidoRequest);
    List<ItemPedidoEntity> toItemPedidoEntityList(List<ItemPedidoRequest> itemPedidoRequestList);
}
