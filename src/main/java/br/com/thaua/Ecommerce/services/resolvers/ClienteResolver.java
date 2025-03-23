package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class ClienteResolver extends AbstractResolver<ClienteEntity> implements ResolverUsers{
    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.CLIENTE);
    }

    @Override
    public Object identificarUsers(UsersEntity usersEntity) {
        if(usersEntity.getCliente() == null) {
            ClienteEntity clienteEntity = new ClienteEntity();
            usersEntity.setCliente(clienteEntity);
            setInformationEntity(clienteEntity, usersEntity);

            return usersEntity;
        }

        return usersEntity.getCliente();
    }
}
