package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.CodigoVerificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoVerificacaoRepository extends JpaRepository<CodigoVerificacaoEntity, Long> {
    CodigoVerificacaoEntity findByCodigo(int codigo);
}
