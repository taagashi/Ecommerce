package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.FornecedorRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FornecedorResolver extends AbstractResolver<FornecedorEntity> implements ResolverUsers{
    private final UsersRepository usersRepository;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.FORNECEDOR);
    }

    @Override
    public Object identificarUsers(UsersEntity usersEntity) {
        if(usersEntity.getFornecedor() == null) {
            FornecedorEntity fornecedorEntity = new FornecedorEntity();
            usersEntity.setFornecedor(fornecedorEntity);
            setInformationEntity(fornecedorEntity, usersEntity);

            return usersEntity;
        }

        return usersEntity.getFornecedor();
    }

}
