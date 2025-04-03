package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.EnderecoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.exceptions.AddressException;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import br.com.thaua.Ecommerce.mappers.UserMapper;
import br.com.thaua.Ecommerce.repositories.EnderecoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ResolverGeralUsers;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final EnderecoMapper enderecoMapper;
    private final EnderecoRepository enderecoRepository;
    private final ValidationService validationService;

    public String cadastrarUsuario(UsersRequest usuario) {
//        TALVES CRIAR UMA CLASSE QUE CODIFICA E DECODIFICA UMA SENHA
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsersEntity usersEntity = userMapper.toEntity(usuario);

        resolverGeralUsers.setInformationUsers(usersEntity);
        UsersEntity saveUser = usersRepository.save(usersEntity);
//        emailMessageService.registroDeUsuario(usuario.getName(), usuario.getEmail());
        return jwtService.generateToken(new MyUserDetails(saveUser.getId(), saveUser.getEmail(), saveUser.getPassword(), saveUser.getRole().name(), saveUser));
    }

    public String login(String email, String password) {
        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if(authenticateUser == null) {
            throw new RuntimeException("Erro de autenticação");
        }

        return jwtService.generateToken((MyUserDetails) authenticateUser.getPrincipal());
    }

//    CRIAR LOGICA DE IMPEDIMENTO DE DELETACAO DE CONTA
    public String deletarConta() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersRepository.delete(usersEntity);
        return usersEntity.getName() + " sua conta foi deletada com sucesso";
    }

    public EnderecoResponse cadastrarEndereco(EnderecoRequest enderecoRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarEnderecoExistente(usersEntity);
        validationService.validarSiglaEstado(enderecoMapper, enderecoRequest, usersEntity);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity);
        usersEntity.setEndereco(enderecoEntity);

        return enderecoMapper.toEnderecoResponse(usersRepository.save(usersEntity).getEndereco());
    }

    public EnderecoResponse exibirEndereco() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getEndereco() == null) {
            throw new RuntimeException(usersEntity.getName() + ", você ainda não cadastrou um endereço");
        }
        return enderecoMapper.toEnderecoResponse(usersEntity.getEndereco());
    }

    public String deletarEndereco() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getEndereco() == null) {
            throw new RuntimeException(usersEntity.getName() + ", não é possivel limpar as informações do seu endereço porque você aida não adicionou um");
        }

        EnderecoEntity enderecoEntity = usersEntity.getEndereco();
        usersEntity.getEndereco().setUsers(null);
        usersEntity.setEndereco(null);

        usersRepository.save(usersEntity);
        enderecoRepository.delete(enderecoEntity);
        return usersEntity.getName() + ", as informacoes do seu endereco foram deletadas com sucesso";
    }

    public EnderecoResponse atualizarEndereco(EnderecoRequest enderecoRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity);
        enderecoEntity.setId(usersEntity.getEndereco().getId());
        usersEntity.setEndereco(enderecoEntity);

        return enderecoMapper.toEnderecoResponse(usersRepository.save(usersEntity).getEndereco());
    }
}
