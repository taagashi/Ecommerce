package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.AdminRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminResolver extends AbstractResolver<AdminEntity> implements ResolverUsers {
    private final UsersRepository usersRepository;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.ADMIN);
    }

    @Override
    public Object identificarUsers(UsersEntity usersEntity) {
        if(usersEntity.getAdmin() == null) {
            AdminEntity adminEntity = new AdminEntity();
            usersEntity.setAdmin(adminEntity);
            setInformationEntity(adminEntity, usersEntity);

            return usersEntity;
        }

        return usersEntity.getAdmin();
    }

    @Override
    public String deletarConta() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdminEntity adminEntity = (AdminEntity) myUserDetails.getTypeUser();
        usersRepository.delete(adminEntity.getUsers());
        return adminEntity.getName() + " sua conta foi deletada com sucesso";
    }
}
