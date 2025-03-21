package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import org.springframework.stereotype.Component;

@Component
public interface ResolverUsers {
    boolean roleEsperada(Role role);
    Object identificarUsers(UsersEntity users);
}
