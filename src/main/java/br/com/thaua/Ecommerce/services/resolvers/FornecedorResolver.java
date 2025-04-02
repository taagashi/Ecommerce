package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FornecedorResolver extends AbstractResolver<FornecedorEntity> implements ResolverUsers{
    private final UsersRepository usersRepository;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.FORNECEDOR);
    }

    @Override
    public void trackUserForRegister(UsersEntity usersEntity) {
        FornecedorEntity fornecedorEntity = new FornecedorEntity();
        usersEntity.setFornecedor(fornecedorEntity);
        setInformationEntity(fornecedorEntity, usersEntity);
    }

}
