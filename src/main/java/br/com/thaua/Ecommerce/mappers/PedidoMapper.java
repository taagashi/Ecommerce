package br.com.thaua.Ecommerce.mappers;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import br.com.thaua.Ecommerce.domain.entity.PedidoEntity;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    @Mapping(target = "cliente", source = "cliente.name")
    @Mapping(target = "pedidoId", source = "id")
    PedidoResponse toPedidoResponse(PedidoEntity pedidoEntity);

    @Mapping(target = "produto", expression = "java(itemPedidoEntity.getProduto().getNome())")
    @Mapping(target = "itemPedidoId", source = "id")
    ItemPedidoResponse toItemPedidoResponse(ItemPedidoEntity itemPedidoEntity);
    List<ItemPedidoResponse> toItemPedidoResponseList(List<ItemPedidoEntity> itemPedidoEntityList);
}
