package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ResolverUsers;
import br.com.thaua.Ecommerce.services.resolvers.ReturnTyUsers;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final ReturnTyUsers returnTyUsers;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsersEntity usersEntity = usersRepository.findByEmail(email);

        if (usersEntity == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        Object typeUser = returnTyUsers.returnTypeUsers(usersEntity);

        return new MyUserDetails(usersEntity.getEmail(), usersEntity.getPassword(), usersEntity.getRole().name(), typeUser);
    }
}
