package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    Optional<ProdutoEntity> findByIdAndFornecedorId(Long idProduto, Long idFornecedor);
    Page<ProdutoEntity> findAllByFornecedorId(Long fornecedorId, Pageable pageable);
    Page<ProdutoEntity> findAllByPrecoGreaterThan(BigDecimal min, Pageable pageable);
    Page<ProdutoEntity> findAllByPrecoLessThan(BigDecimal max, Pageable pageable);
    Page<ProdutoEntity> findAllByPrecoBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    Page<ProdutoEntity> findAllByFornecedorIdAndQuantidadeDemandaGreaterThan(Long fornecedorId, Integer quantidadeDemanda, Pageable pageable);
}
