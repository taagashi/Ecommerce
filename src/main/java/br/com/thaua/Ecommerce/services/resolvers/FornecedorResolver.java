package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class FornecedorResolver implements ResolverUsers{
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.FORNECEDOR);
    }

    @Override
    public void trackUserForRegister(UsersEntity usersEntity) {
        FornecedorEntity fornecedorEntity = new FornecedorEntity();
        fornecedorEntity.setUsers(usersEntity);
        usersEntity.setFornecedor(fornecedorEntity);
    }

    @Override
    public void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("fornecedores")).clear();
    }

}
