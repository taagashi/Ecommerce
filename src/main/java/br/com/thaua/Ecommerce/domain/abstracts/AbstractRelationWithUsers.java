package br.com.thaua.Ecommerce.domain.abstracts;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractRelationWithUsers extends AbstractEntity {
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private UsersEntity users;
}
