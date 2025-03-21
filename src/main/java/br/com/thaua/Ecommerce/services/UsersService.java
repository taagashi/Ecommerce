package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
import br.com.thaua.Ecommerce.mappers.Converter;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ReturnTyUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final Converter converter;
    private final UsersRepository usersRepository;
    private final ReturnTyUsers returnTyUsers;

    public UsersResponse cadastrarUsuario(UsersRequest usuario) {
        UsersEntity usersEntity = converter.toEntity(usuario);

        UsersEntity typeUser = (UsersEntity) returnTyUsers.returnTypeUsers(usersEntity);

        return converter.toResponse(usersRepository.save(typeUser));
    }

}
