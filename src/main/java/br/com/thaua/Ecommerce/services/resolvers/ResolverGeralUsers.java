package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.exceptions.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResolverGeralUsers {
    private final List<ResolverUsers> resolverUsers;

    public void setInformationUsers(UsersEntity usersEntity) {
        resolverUsers.stream()
                .filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> throwExceptionRole(usersEntity))
                .trackUserForRegister(usersEntity);
    }

    public void cleanCache(UsersEntity usersEntity) {
        resolverUsers.stream().filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> throwExceptionRole(usersEntity))
                .cleanCache(usersEntity);
    }

    public Object viewProfile(UsersEntity usersEntity) {
        return resolverUsers.stream().filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> throwExceptionRole(usersEntity))
                .viewProfile(usersEntity);
    }

    private RuntimeException throwExceptionRole(UsersEntity usersEntity) {
        throw new RoleNotFoundException("Role não encontrada", Map.of("Role incorreta", usersEntity.getName() + " você inseriu a role incorretamente"));
    }

}
