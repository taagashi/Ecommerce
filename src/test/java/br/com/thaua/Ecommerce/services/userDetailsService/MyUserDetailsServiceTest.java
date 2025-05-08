package br.com.thaua.Ecommerce.services.userDetailsService;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.MyUserDetailsService;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {
    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ValidationService validationService;

    @DisplayName("Deve retornar com sucesso usuario autenticado")
    @Test
    public void testLoadUserByUsername() {
        Long userId = 1L;
        String email = "taagashi.dev@gmail.com";
        UsersEntity usersEntity = Fixture.createUsersEntity(userId, "taagashi", email, "0000000-0000", "senha", Role.ADMIN, null, null, null, null);

        when(usersRepository.findByEmail(email)).thenReturn(usersEntity);

        MyUserDetails myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(email);

        List<SimpleGrantedAuthority>  simpleGrantedAuthorities= (List<SimpleGrantedAuthority>) myUserDetails.getAuthorities().stream().toList();

        assertThat(myUserDetails.getId()).isEqualTo(usersEntity.getId());
        assertThat(myUserDetails.getUsername()).isEqualTo(usersEntity.getEmail());
        assertThat(myUserDetails.getPassword()).isEqualTo(usersEntity.getPassword());
        assertThat(simpleGrantedAuthorities.getFirst().getAuthority()).isEqualTo("ROLE_" + usersEntity.getRole().toString());
    }

    @DisplayName("Deve retornar UserNotFoundException ao tentar bucar usuario autenticado")
    @Test
    public void testLoadUserByError() {
        String errorMessage = "Não foi possivel fazer login";
        Map<String, String> errors = Map.of("Falha de busca", "Item não encontrado");
        String emailError = null;

        when(usersRepository.findByEmail(emailError)).thenReturn(null);

        doThrow(new UserNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, UserNotFoundException.class, Map.of());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> myUserDetailsService.loadUserByUsername(emailError));

        assertThat(userNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(userNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));
    }
}
