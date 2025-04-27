    package br.com.thaua.Ecommerce.services.resolvers;

    import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
    import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
    import br.com.thaua.Ecommerce.domain.enums.Role;
    import br.com.thaua.Ecommerce.mappers.AdminMapper;
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
        private final AdminMapper adminMapper;

        @Override
        public boolean roleEsperada(Role role) {
            log.info("ADMIN RESOLVER - ROLE ESPERADA");
            return role.equals(Role.ADMIN);
        }

        @Override
        public void trackUserForRegister(UsersEntity usersEntity) {
            log.info("ADMIN RESOLVER - TRACK USER FOR REGISTER");
            AdminEntity adminEntity = new AdminEntity();
            adminEntity.setUsers(usersEntity);
            usersEntity.setAdmin(adminEntity);
        }

        @Override
        public void cleanCache(UsersEntity usersEntity) {
            log.info("ADMIN RESOLVER - CLEAN CACHE");
            Objects.requireNonNull(cacheManager.getCache("adminsListagem")).clear();
            log.info("CACHE ADMINSLISTAGEM FOI LIMPO");
        }

        @Override
        public Object viewProfile(UsersEntity usersEntity) {
            log.info("ADMIN RESOLVER - VIEW PROFILE");
            return adminMapper.adminEntityToAdminResponse(usersEntity.getAdmin());
        }

    }
