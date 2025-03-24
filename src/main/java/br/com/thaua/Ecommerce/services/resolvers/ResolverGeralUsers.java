package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResolverGeralUsers {
    private final List<ResolverUsers> resolverUsers;

    public Object returnTypeUsers(UsersEntity usersEntity) {
        return resolverUsers.stream()
                .filter(user -> user.roleEsperada(usersEntity.getRole()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role não encontrada"))
                .identificarUsers(usersEntity);
    }
}
