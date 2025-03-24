package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.dto.users.UsersResponse;
import br.com.thaua.Ecommerce.mappers.UserMapper;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ResolverGeralUsers;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final ResolverGeralUsers resolverGeralUsers;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailMessageService emailMessageService;
    private final UserMapper userMapper;

    public UsersResponse cadastrarUsuario(UsersRequest usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsersEntity usersEntity = userMapper.toEntity(usuario);

        UsersEntity typeUser = (UsersEntity) resolverGeralUsers.returnTypeUsers(usersEntity);

//        emailMessageService.registroDeUsuario(usuario.getName(), usuario.getEmail());
        return userMapper.toResponse(usersRepository.save(typeUser));
    }

    public String login(String email, String password) {
        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if(authenticateUser == null) {
            throw new RuntimeException("Erro de autenticação");
        }

        return jwtService.generateToken((MyUserDetails) authenticateUser.getPrincipal());
    }

    public String deletarConta() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersRepository.delete(usersEntity);
        return usersEntity.getName() + " sua conta foi deletada com sucesso";
    }
}
