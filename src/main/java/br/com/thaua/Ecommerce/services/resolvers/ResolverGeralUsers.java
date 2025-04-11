package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResolverGeralUsers {
    private final List<ResolverUsers> resolverUsers;

    public void setInformationUsers(UsersEntity usersEntity) {
        resolverUsers.stream()
                .filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role não encontrada"))
                .trackUserForRegister(usersEntity);
    }

    public void clearCache(UsersEntity usersEntity) {
        resolverUsers.stream().filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role não encontrada"))
                .clearCache();
    }

}
