package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    PedidoEntity findByIdAndClienteId(Long pedidoId, Long id);
}
