package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CodigoVerificacaoEntity;
import br.com.thaua.Ecommerce.domain.entity.EnderecoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.exceptions.AddressException;
import br.com.thaua.Ecommerce.exceptions.CodeNotValidException;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import br.com.thaua.Ecommerce.mappers.UserMapper;
import br.com.thaua.Ecommerce.repositories.CodigoVerificacaoRepository;
import br.com.thaua.Ecommerce.repositories.EnderecoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.resolvers.ResolverGeralUsers;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
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
    private final CodigoVerificacaoRepository codigoVerificacaoRepository;

    @Transactional
    public String cadastrarUsuario(UsersRequest usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        UsersEntity usersEntity = userMapper.toEntity(usuario);

        resolverGeralUsers.setInformationUsers(usersEntity);
        UsersEntity saveUser = usersRepository.save(usersEntity);
//        resolverGeralUsers.cleanCache(usersEntity);

//        emailMessageService.registroDeUsuario(usuario.getName(), usuario.getEmail());
        log.info("SERVICE USERS - CADASTRAR USUARIO");
        return jwtService.generateToken(new MyUserDetails(saveUser.getId(), saveUser.getEmail(), saveUser.getPassword(), saveUser.getRole().name(), saveUser));
    }

    public String login(String email, String password) {
        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        MyUserDetails myUserDetails = (MyUserDetails) authenticateUser.getPrincipal();

//        resolverGeralUsers.cleanCache(myUserDetails.getUser());

        log.info("SERVICE USERS - LOGIN");
        return jwtService.generateToken(myUserDetails);
    }

//    CRIAR LOGICA DE IMPEDIMENTO DE DELETACAO DE CONTA
    @Transactional
    public String deletarConta() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersRepository.delete(usersEntity);

//        resolverGeralUsers.cleanCache(usersEntity);

        log.info("SERVICE USERS - DELETAR CONTA");
        return usersEntity.getName() + " sua conta foi deletada com sucesso";
    }

    public EnderecoResponse cadastrarEndereco(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarCadastroEndereco(usersEntity, errors);
        validationService.validarSiglaEstado(enderecoRequest, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro no cadastrado do endereco", AddressException.class, errors);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity);
        usersEntity.setEndereco(enderecoEntity);

        log.info("SERVICE USERS - CADASTRAR ENDERECO");
        return enderecoMapper.toEnderecoResponse(usersRepository.save(usersEntity).getEndereco());
    }

    public EnderecoResponse exibirEndereco(Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro durante a exibição do seu endereço", AddressException.class, errors);

        log.info("SERVICE USERS - EXIBIR ENDERECO");
        return enderecoMapper.toEnderecoResponse(usersEntity.getEndereco());
    }

    public String deletarEndereco(Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarDelecaoEndereco(usersEntity, errors);
        validationService.analisarException(usersEntity.getName() + " você não pode limpar as informações do seu endereco porque ainda não adicionou um", AddressException.class, errors);

        EnderecoEntity enderecoEntity = usersEntity.getEndereco();
        usersEntity.getEndereco().setUsers(null);
        usersEntity.setEndereco(null);

        usersRepository.save(usersEntity);
        enderecoRepository.delete(enderecoEntity);

        log.info("SERVICE USERS - DELETAR ENDERECO");
        return usersEntity.getName() + ", as informacoes do seu endereco foram deletadas com sucesso";
    }

    @Transactional
    public EnderecoResponse atualizarEndereco(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarAtualizacaoEndereco(usersEntity, errors);
        validationService.validarSiglaEstado(enderecoRequest, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro na atualização do seu endereço", AddressException.class, errors);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity);
        enderecoEntity.setId(usersEntity.getEndereco().getId());
        usersEntity.setEndereco(enderecoEntity);

        log.info("SERVICE USERS - ATUALIZAR ENDERECO");
        return enderecoMapper.toEnderecoResponse(usersRepository.save(usersEntity).getEndereco());
    }

    @Transactional
    public String gerarCodigoRedefinirSenha(UserRequestGenerateCode userRequestGenerateCode, Map<String, String> errors) {
        validationService.validarExistenciaEmail(userRequestGenerateCode.getEmail(), errors);
        validationService.analisarException("Houve um erro ao tentar gerar codigo para redefinir senha", UserNotFoundException.class, errors);

        codigoVerificacaoRepository.save(new CodigoVerificacaoEntity(emailMessageService.gerarCodigoRedefinirSenha(userRequestGenerateCode.getEmail())));

        log.info("SERVICE USERS - GERAR CODIGO DE VERIFICACAO");
        return "Foi enviado um codigo de verificação para " + userRequestGenerateCode.getEmail();
    }

    @Transactional
    public String verificarCodigoRedefirnirSenha(UserRequestGenerateNewPassword userRequestGenerateNewPassword, Map<String, String> errors) {
        UsersEntity usersEntity = validationService.validarExistenciaEmail(userRequestGenerateNewPassword.getEmail(), errors);
        CodigoVerificacaoEntity codigoVerificacaoEntity = validationService.validarCodigoVerificacao(userRequestGenerateNewPassword.getCode(), errors);
        validationService.analisarException("Houve um erro ao tentar redefinir senha", CodeNotValidException.class, errors);

        usersEntity.setPassword(passwordEncoder.encode(userRequestGenerateNewPassword.getNewPassword()));
        usersRepository.save(usersEntity);
        codigoVerificacaoRepository.delete(codigoVerificacaoEntity);

        log.info("SERVICE USERS - VERIFICAR CODIGO DE VERIFICACAO");
        return usersEntity.getName() + " sua senha foi redefinida com sucesso";
    }

    public Object exibirPerfil() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        log.info("SERVICE USERS - EXIBIR PERFIL");
        return resolverGeralUsers.viewProfile(usersEntity);
    }
}
