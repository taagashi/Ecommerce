package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    Optional<PedidoEntity> findByIdAndClienteId(Long pedidoId, Long id);
    Page<PedidoEntity> findAllByClienteId(Long clienteId, Pageable pageable);
}
