package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    Optional<ProdutoEntity> findByIdAndFornecedorId(Long idProduto, Long idFornecedor);
}
