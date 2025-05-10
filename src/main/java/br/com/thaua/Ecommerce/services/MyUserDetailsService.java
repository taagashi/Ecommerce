package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final ValidationService validationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsersEntity usersEntity = usersRepository.findByEmail(email);

        Map<String, String> errors = ConstructorErrors.returnMapErrors();
        validationService.validarExistenciaEntidade(usersEntity, errors, "Usuario");
        validationService.analisarException("Não foi possivel fazer login", UserNotFoundException.class, errors);

        log.info("SERVICE - MYUSERDETAILSSERVICE - LOAD USER BY USERNAME");
        return new MyUserDetails(usersEntity.getId(), usersEntity.getEmail(), usersEntity.getPassword(), usersEntity.getRole().name(), usersEntity);
    }
}
