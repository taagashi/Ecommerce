package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {
    Optional<ItemPedidoEntity> findByIdAndPedidoClienteId(Long itemPedidoId, Long clienteId);
}
