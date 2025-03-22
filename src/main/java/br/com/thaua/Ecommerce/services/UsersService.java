package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
import br.com.thaua.Ecommerce.mappers.Converter;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ReturnTyUsers;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final Converter converter;
    private final UsersRepository usersRepository;
    private final ReturnTyUsers returnTyUsers;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailMessageService emailMessageService;

    public UsersResponse cadastrarUsuario(UsersRequest usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsersEntity usersEntity = converter.toEntity(usuario);

        UsersEntity typeUser = (UsersEntity) returnTyUsers.returnTypeUsers(usersEntity);

//        PRECISO COLOCAR UMA MENSAGEM MELHOR AQUI
        emailMessageService.enviarEmails("Registro Ecommerce", "Parabéns " + usersEntity.getName() + " voce acaba de se registrar no nosso Ecommerce :)", usersEntity.getEmail());
        return converter.toResponse(usersRepository.save(typeUser));
    }

    public String login(String email, String password) {
        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if(authenticateUser == null) {
            throw new RuntimeException("Erro de autenticação");
        }

        return jwtService.generateToken((MyUserDetails) authenticateUser.getPrincipal());
    }
}
