package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<FornecedorEntity, Long> {
}
