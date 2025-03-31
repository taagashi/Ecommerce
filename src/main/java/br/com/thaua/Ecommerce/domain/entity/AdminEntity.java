package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractRelationWithUsers;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AdminEntity extends AbstractRelationWithUsers {
    private Integer contasBanidas;

    @UpdateTimestamp
    private LocalDateTime ultimoAcesso;

}

