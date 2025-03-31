package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {
    ItemPedidoEntity findByIdAndPedidoClienteId(Long itemPedidoId, Long clienteId);
}
