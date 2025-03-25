package br.com.thaua.Ecommerce.repositories;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
}
