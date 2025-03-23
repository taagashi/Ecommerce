package br.com.thaua.Ecommerce.services.resolvers;

import br.com.thaua.Ecommerce.domain.entity.AdminEntity;
import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ClienteResolver extends AbstractResolver<ClienteEntity> implements ResolverUsers{
    private final UsersRepository usersRepository;

    @Override
    public boolean roleEsperada(Role role) {
        return role.equals(Role.CLIENTE);
    }

    @Override
    public Object identificarUsers(UsersEntity usersEntity) {
        if(usersEntity.getCliente() == null) {
            ClienteEntity clienteEntity = new ClienteEntity();
            usersEntity.setCliente(clienteEntity);
            setInformationEntity(clienteEntity, usersEntity);

            return usersEntity;
        }

        return usersEntity.getCliente();
    }

    @Override
    public String deletarConta() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClienteEntity clienteEntity = (ClienteEntity) myUserDetails.getTypeUser();

        if(!clienteEntity.getPedido().isEmpty()) {
            throw new RuntimeException(clienteEntity.getName() + " voce nao pode deletar sua conta porque voce ainda tem pedidos pendentes");
        }

        usersRepository.delete(clienteEntity.getUsers());
        return clienteEntity.getName() + " sua conta foi deletada com sucesso";
    }
}
