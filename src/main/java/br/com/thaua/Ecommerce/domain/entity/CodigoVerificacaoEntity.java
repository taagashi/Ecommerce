package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class CodigoVerificacaoEntity extends AbstractEntity {
    private int codigo;
}
