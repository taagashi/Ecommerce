package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class FornecedorResolver implements ResolverUsers{
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final FornecedorMapper fornecedorMapper;

    @Override
    public boolean roleEsperada(Role role) {
        log.info("FORNECEDOR RESOLVER - ROLE ESPERADA");
        return role.equals(Role.FORNECEDOR);
    }

    @Override
    public void rastrearUsuarioParaRegistro(UsersEntity usersEntity) {
        log.info("FORNECEDOR RESOLVER - TRACK USER FOR REGISTER");
        FornecedorEntity fornecedorEntity = new FornecedorEntity();
        fornecedorEntity.setUsers(usersEntity);
        usersEntity.setFornecedor(fornecedorEntity);
    }

    @Override
    public void limparCache(UsersEntity usersEntity) {
        log.info("FORNECEDOR RESOLVER - CLEAN CACHE");
        Objects.requireNonNull(cacheManager.getCache("fornecedoresListagem")).clear();
        log.info("CACHE FORNECEDORESLITAGEM FOI LIMPO");
    }

    @Override
    public Object exibirPerfil(UsersEntity usersEntity) {
        log.info("FORNECEDOR RESOLVER - VIEW PROFILE");
        return fornecedorMapper.fornecedorEntityToFornecedorViewProfileResponse(usersEntity.getFornecedor());
    }

}
