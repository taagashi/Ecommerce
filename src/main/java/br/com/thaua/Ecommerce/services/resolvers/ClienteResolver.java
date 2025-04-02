package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClienteResolver extends AbstractResolver<ClienteEntity> implements ResolverUsers{
    private final UsersRepository usersRepository;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.CLIENTE);
    }

    @Override
    public void trackUserForRegister(UsersEntity usersEntity) {
        ClienteEntity clienteEntity = new ClienteEntity();
        usersEntity.setCliente(clienteEntity);
        setInformationEntity(clienteEntity, usersEntity);

    }

}
