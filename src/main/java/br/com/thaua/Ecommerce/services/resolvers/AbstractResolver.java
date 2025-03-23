package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractCommonData;
import br.com.thaua.Ecommerce.domain.abstracts.AbstractRelationWithUsers;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import org.springframework.stereotype.Component;

public class AbstractResolver <T extends AbstractRelationWithUsers>{
     public void setInformationEntity(T entity, UsersEntity usersEntity) {
        entity.setUsers(usersEntity);
        entity.setName(usersEntity.getName());
        entity.setEmail(usersEntity.getEmail());
    }
}
