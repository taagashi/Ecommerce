package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class AdminResolver extends AbstractResolver<AdminEntity> implements ResolverUsers {
    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.ADMIN);
    }

    @Override
    public Object identificarUsers(UsersEntity usersEntity) {
        if(usersEntity.getAdmin() == null) {
            AdminEntity adminEntity = new AdminEntity();
            usersEntity.setAdmin(adminEntity);
            setInformationEntity(adminEntity, usersEntity);

            return usersEntity;
        }

        return usersEntity.getAdmin();
    }
}
