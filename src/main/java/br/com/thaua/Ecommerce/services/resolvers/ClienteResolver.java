package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClienteResolver implements ResolverUsers{
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.CLIENTE);
    }

    @Override
    public void trackUserForRegister(UsersEntity usersEntity) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setUsers(usersEntity);
        usersEntity.setCliente(clienteEntity);
    }

    @Override
    public void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("clientes")).clear();
        log.info("Cache de clientes foi limpo");
    }

}
