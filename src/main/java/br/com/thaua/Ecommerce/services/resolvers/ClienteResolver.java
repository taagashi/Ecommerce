package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
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
    private final ClienteMapper clienteMapper;

    @Override
    public boolean roleEsperada(Role role) {
        log.info("CLIENTE RESOLVER - ROLE ESPERADA");
        return role.equals(Role.CLIENTE);
    }

    @Override
    public void trackUserForRegister(UsersEntity usersEntity) {
        log.info("CLIENTE RESOLVER - TRACK USER FOR REGISTER");
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setUsers(usersEntity);
        usersEntity.setCliente(clienteEntity);
    }

    @Override
    public void cleanCache(UsersEntity usersEntity) {
        log.info("CLIENTE RESOLVER - CLEAN CACHE");
        Objects.requireNonNull(cacheManager.getCache("clientesListagem")).clear();
        log.info("CACHE CLIENTESLISTAGEM LIMPO");
    }

    @Override
    public Object viewProfile(UsersEntity usersEntity) {
         log.info("CLIENTE RESOLVER - VIEW PROFILE");
         return clienteMapper.toResponseComPedido(usersEntity.getCliente());
    }

}
