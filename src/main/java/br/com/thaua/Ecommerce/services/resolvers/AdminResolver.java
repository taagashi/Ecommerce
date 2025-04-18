    package br.com.thaua.Ecommerce.services.resolvers;

    import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
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
    public class AdminResolver implements ResolverUsers {
        private final UsersRepository usersRepository;
        private final CacheManager cacheManager;

        @Override
        public boolean roleEsperada(Role role) {
            return role.equals(Role.ADMIN);
        }

        @Override
        public void trackUserForRegister(UsersEntity usersEntity) {
            AdminEntity adminEntity = new AdminEntity();
            adminEntity.setUsers(usersEntity);
            usersEntity.setAdmin(adminEntity);
        }

        @Override
        public void cleanCache(UsersEntity usersEntity) {
            Objects.requireNonNull(cacheManager.getCache("adminsListagem")).clear();
            log.info("CACHE ADMINSLISTAGEM FOI LIMPO");
        }

    }
