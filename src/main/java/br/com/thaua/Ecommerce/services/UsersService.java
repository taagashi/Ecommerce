package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.Users;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public Users cadastrarUsuario(Users usuario) {
        return usersRepository.save(usuario);
    }

}
