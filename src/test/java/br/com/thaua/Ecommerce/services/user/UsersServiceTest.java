package br.com.thaua.Ecommerce.services.user;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.CodigoVerificacaoEntity;
import br.com.thaua.Ecommerce.domain.entity.EnderecoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.domain.enums.Role;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.exceptions.*;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import br.com.thaua.Ecommerce.mappers.UserMapper;
import br.com.thaua.Ecommerce.repositories.CodigoVerificacaoRepository;
import br.com.thaua.Ecommerce.repositories.EnderecoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.EmailMessageService;
import br.com.thaua.Ecommerce.services.JWTService;
import br.com.thaua.Ecommerce.services.UsersService;
import br.com.thaua.Ecommerce.services.resolvers.ResolverGeralUsers;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ResolverGeralUsers resolverGeralUsers;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailMessageService emailMessageService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private CodigoVerificacaoRepository codigoVerificacaoRepository;

    @Mock
    private ExtractTypeUserContextHolder extractTypeUserContextHolder;

    @Mock private Map<String, String> errors;

    @BeforeEach
    public void setUp() {
        errors = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso um token de acesso ao fazer cadastro de usuario")
    @Test
    public void testCadastrarUsuarioSucesso() {
        Long userId = 4L;
        String name = "joao";
        String email = "joao@gmail.com";
        String password = "senha123";
        Role role = Role.FORNECEDOR;
        String jwtToken = UUID.randomUUID().toString();


        UsersRequest usersRequest = Fixture.createUserRequest(name, email, password, role.toString());
        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, "+0000000-0000", password, role, null, null, null, null);


        when(passwordEncoder.encode(usersRequest.getPassword())).thenReturn(usersRequest.getPassword());
        when(userMapper.toEntity(usersRequest)).thenReturn(usersEntity);
        when(usersRepository.save(usersEntity)).thenReturn(usersEntity);
        when(jwtService.generateToken(any(MyUserDetails.class))).thenReturn(jwtToken);

        String jwtTokenCompare = usersService.cadastrarUsuario(usersRequest);

        assertThat(jwtTokenCompare).isEqualTo(jwtToken);
    }

    @DisplayName("Deve retornar com sucesso um token de acesso ao fazer login")
    @Test
    public void testLoginSucesso() {
        Long userId = 10L;
        String name = "jorlan";
        String email = "jorlan@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.CLIENTE;
        String password = "senha22";
        String jwtToken = UUID.randomUUID().toString();

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        MyUserDetails myUserDetails = new MyUserDetails(userId, email, password, role.toString(), usersEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(myUserDetails, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtService.generateToken(any(MyUserDetails.class))).thenReturn(jwtToken);

        String jwtTokenCompare = usersService.login(email, password);

        assertThat(jwtTokenCompare).isEqualTo(jwtToken);
    }

    @DisplayName("Deve retornar UsernotFoundException apos erro no login")
    @Test
    public void testLoginError() {
        String errorMessage = "Não foi possivel fazer login";
        errors.put("Falha de busca", "Item não encontrado");
        String emailError = "email incorreto";
        String password = "23123";

        doThrow(new UserNotFoundException(errorMessage, errors)).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> usersService.login(emailError, password));

        assertThat(userNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(userNotFoundException.getFields().get("Falha de busca")).isEqualTo(errors.get("Falha de busca"));
    }

    @DisplayName("Deve retornar com sucesso mensagem de delecao de conta")
    @Test
    public void testDeletarContaSucesso() {
        Long userId = 10L;
        String name = "jorlan";
        String email = "jorlan@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.CLIENTE;
        String password = "senha22";

        String messageSucesso = "jorlan sua conta foi deletada com sucesso";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        String messageSucessoCompare = usersService.deletarConta();

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar com sucesso endereco cadastrado")
    @Test
    public void testCadastrarEnderecoSucesso() {
        Long userId = 5L;
        String name = "monica";
        String email = "monica@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.FORNECEDOR;
        String password = "senha123";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        Estado estado = Estado.AC;
        String cep = "cep";

        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest(rua, numero, bairro, cidade, estado.getSigla(), cep);
        EnderecoEntity enderecoEntity = Fixture.createEnderecoEntity(rua, numero, bairro, cidade, estado, cep, usersEntity);
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, name, rua, numero, bairro, cidade, estado.getSigla(), cep);

        usersEntity.setEndereco(enderecoEntity);
        enderecoEntity.setUsers(usersEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(enderecoMapper.enderecoRequestToEntity(enderecoRequest)).thenReturn(enderecoEntity);
        when(enderecoMapper.toEnderecoResponse(enderecoEntity)).thenReturn(enderecoResponse);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);

        EnderecoResponse enderecoResponseCompare = usersService.cadastrarEndereco(enderecoRequest, errors);

        assertThat(enderecoResponseCompare.getIdEndereco()).isEqualTo(enderecoResponse.getIdEndereco());
        assertThat(enderecoResponseCompare.getNameUser()).isEqualTo(enderecoResponse.getNameUser());
        assertThat(enderecoResponseCompare.getRua()).isEqualTo(enderecoResponse.getRua());
        assertThat(enderecoResponseCompare.getNumero()).isEqualTo(enderecoResponse.getNumero());
        assertThat(enderecoResponseCompare.getBairro()).isEqualTo(enderecoResponse.getBairro());
        assertThat(enderecoResponseCompare.getCidade()).isEqualTo(enderecoResponse.getCidade());
        assertThat(enderecoResponseCompare.getEstado()).isEqualTo(enderecoResponse.getEstado());
        assertThat(enderecoResponseCompare.getCep()).isEqualTo(enderecoResponse.getCep());
    }

    @DisplayName("Deve retornar CadastroEnderecoException apos erro no cadastro de endereco (possivel endereco ja existente ou sigla invalida)")
    @Test
    public void testDeletarContaError() {
        Long userId = 10L;
        String name = "adriana";
        String email = "adriana@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.ADMIN;
        String password = "senha123";


        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        Estado estado = Estado.AC;
        String cep = "cep";

        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest(rua, numero, bairro, cidade, estado.getSigla(), cep);

        String errorMessage = name + ", houve um erro no cadastrado do endereco";
        errors.put("Endereço", "Endereço já cadastrado");
        errors.put("Estado", "Sigla de estado inválido");

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new CadastroEnderecoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, CadastroEnderecoException.class, errors);

        CadastroEnderecoException cadastroEnderecoException = assertThrows(CadastroEnderecoException.class, () ->usersService.cadastrarEndereco(enderecoRequest, errors));

        assertThat(cadastroEnderecoException.getMessage()).isEqualTo(errorMessage);
        assertThat(cadastroEnderecoException.getFields().get("Endereço")).isEqualTo(errors.get("Endereço"));
        assertThat(cadastroEnderecoException.getFields().get("Status")).isEqualTo(errors.get("Status"));
    }

    @DisplayName("Deve retornar com sucesso endereco exibido")
    @Test
    public void testExibirEnderecoSucesso() {
        Long userId = 2L;
        String name = "jonatas";
        String email = "jonatas@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.FORNECEDOR;
        String password = "senha123";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        Estado estado = Estado.AC;
        String cep = "cep";

        EnderecoEntity enderecoEntity = Fixture.createEnderecoEntity(rua, numero, bairro, cidade, estado, cep, null);
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, name, rua, numero, bairro, cidade, estado.getSigla(), cep);

        usersEntity.setEndereco(enderecoEntity);
        enderecoEntity.setUsers(usersEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(enderecoMapper.toEnderecoResponse(enderecoEntity)).thenReturn(enderecoResponse);

        EnderecoResponse enderecoResponseCompare = usersService.exibirEndereco(errors);

        assertThat(enderecoResponseCompare.getIdEndereco()).isEqualTo(enderecoResponse.getIdEndereco());
        assertThat(enderecoResponseCompare.getNameUser()).isEqualTo(enderecoResponse.getNameUser());
        assertThat(enderecoResponseCompare.getRua()).isEqualTo(enderecoResponse.getRua());
        assertThat(enderecoResponseCompare.getNumero()).isEqualTo(enderecoResponse.getNumero());
        assertThat(enderecoResponseCompare.getBairro()).isEqualTo(enderecoResponse.getBairro());
        assertThat(enderecoResponseCompare.getCidade()).isEqualTo(enderecoResponse.getCidade());
        assertThat(enderecoResponseCompare.getEstado()).isEqualTo(enderecoResponse.getEstado());
        assertThat(enderecoResponseCompare.getCep()).isEqualTo(enderecoResponse.getCep());
    }

    @DisplayName("Deve retornar com sucesso mensagem de confimação de delecao de endereco")
    @Test
    public void testDeletarEnderecoSucesso() {
        Long userId = 20L;
        String name = "mago";
        String email = "mago@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.CLIENTE;
        String password = "senha grande";

        String messageSucesso = name + ", as informacoes do seu endereco foram deletadas com sucesso";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        String rua = "armando";
        String numero = "21";
        String bairro = "centro";
        String cidade = "minapolis";
        Estado estado = Estado.SP;
        String cep = "123444-22";

        EnderecoEntity enderecoEntity = Fixture.createEnderecoEntity(rua, numero, bairro, cidade, estado, cep, null);

        usersEntity.setEndereco(enderecoEntity);
        enderecoEntity.setUsers(usersEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);

        String messageSucessoCompare = usersService.deletarEndereco(errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar EnderecoNaoExistenteException apos erro ao deletar endereco")
    @Test
    public void testDeletarEnderecoError() {
        Long userId = 20L;
        String name = "morais";
        String email = "morais@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.ADMIN;
        String password = "123";

        String errorMessage = name + " você não pode limpar as informações do seu endereco porque ainda não adicionou um";
        errors.put("Endereço", "Endereço não cadastrado");

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new EnderecoNaoExistenteException(errorMessage, errors)).when(validationService).analisarException(errorMessage, EnderecoNaoExistenteException.class, errors);

        EnderecoNaoExistenteException enderecoNaoExistenteException = assertThrows(EnderecoNaoExistenteException.class, () -> usersService.deletarEndereco(errors));

        assertThat(enderecoNaoExistenteException.getMessage()).isEqualTo(errorMessage);
        assertThat(enderecoNaoExistenteException.getFields().get("Endereço")).isEqualTo(errors.get("Endereço"));
    }

    @DisplayName("Deve retornar com sucesso endereco atualizado")
    @Test
    public void testAtualizarEnderecoSucesso() {
        Long userId = 20L;
        String name = "morais";
        String email = "morais@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.ADMIN;
        String password = "123";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        Estado estado = Estado.AC;
        String cep = "cep";

        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest(rua, numero, bairro, cidade, estado.getSigla(), cep);
        EnderecoEntity enderecoEntity = Fixture.createEnderecoEntity(rua, numero, bairro, cidade, estado, cep, usersEntity);
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, name, rua, numero, bairro, cidade, estado.getSigla(), cep);

        usersEntity.setEndereco(enderecoEntity);
        enderecoEntity.setUsers(usersEntity);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(enderecoMapper.enderecoRequestToEntity(enderecoRequest)).thenReturn(enderecoEntity);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        when(enderecoMapper.toEnderecoResponse(enderecoEntity)).thenReturn(enderecoResponse);

        EnderecoResponse enderecoResponseCompare = usersService.atualizarEndereco(enderecoRequest, errors);

        assertThat(enderecoResponseCompare.getIdEndereco()).isEqualTo(enderecoResponse.getIdEndereco());
        assertThat(enderecoResponseCompare.getNameUser()).isEqualTo(enderecoResponse.getNameUser());
        assertThat(enderecoResponseCompare.getRua()).isEqualTo(enderecoResponse.getRua());
        assertThat(enderecoResponseCompare.getNumero()).isEqualTo(enderecoResponse.getNumero());
        assertThat(enderecoResponseCompare.getBairro()).isEqualTo(enderecoResponse.getBairro());
        assertThat(enderecoResponseCompare.getCidade()).isEqualTo(enderecoResponse.getCidade());
        assertThat(enderecoResponseCompare.getEstado()).isEqualTo(enderecoResponse.getEstado());
        assertThat(enderecoResponseCompare.getCep()).isEqualTo(enderecoResponse.getCep());
    }

    @DisplayName("Deve retornar AtualizarEnderecoException apos erro ao atualizar endereco")
    @Test
    public void testAtualizarEnderecoError() {
        Long userId = 4L;
        String name = "dantas";
        String email = "dantas@gmail.com";
        String telefone = "+000000-0000";
        Role role = Role.ADMIN;
        String password = "1223";

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        String rua = "rua";
        String numero = "numero";
        String bairro = "bairro";
        String cidade = "cidade";
        Estado estado = Estado.AC;
        String cep = "cep";

        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest(rua, numero, bairro, cidade, estado.getSigla(), cep);

        String errorMessage = name + ", houve um erro na atualização do seu endereço";
        errors.put("Endereço", "Endereço não cadastrado");
        errors.put("Estado", "Sigla de estado inválido");


        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);

        doThrow(new AtualizarEnderecoException(errorMessage, errors)).when(validationService).analisarException(errorMessage, AtualizarEnderecoException.class, errors);

        AtualizarEnderecoException atualizarEnderecoException = assertThrows(AtualizarEnderecoException.class, () -> usersService.atualizarEndereco(enderecoRequest, errors));

        assertThat(atualizarEnderecoException.getMessage()).isEqualTo(errorMessage);
        assertThat(atualizarEnderecoException.getFields().get("Endereço")).isEqualTo(errors.get("Endereço"));
        assertThat(atualizarEnderecoException.getFields().get("Estado")).isEqualTo(errors.get("Estado"));
    }

    @DisplayName("Deve retornar com sucesso codigo de verificacao gerado")
    @Test
    public void testGerarCodigoRedefinirSenhaSucesso() {
        String email = "taagashi.dev@gmail.com";
        String messageSucesso = "Foi enviado um codigo de verificação para " + email;
        int codigoVerificacao = 12345;

        UserRequestGenerateCode userRequestGenerateCode = Fixture.createUserRequestGenerateCode(email);
        CodigoVerificacaoEntity codigoVerificacaoEntity = Fixture.createCodigoVerificacaoEntity(1L, codigoVerificacao);

        when(emailMessageService.gerarCodigoRedefinirSenha(any(String.class))).thenReturn(codigoVerificacao);
        when(codigoVerificacaoRepository.save(any(CodigoVerificacaoEntity.class))).thenReturn(codigoVerificacaoEntity);

        String messageSucessoCompare = usersService.gerarCodigoRedefinirSenha(userRequestGenerateCode, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar UserNotFoundException apos erro ao gerar codigo de verificacao")
    @Test
    public void testGerarCodigoRedefinirSenhaError() {
        String email = "jobson@gmail.com";
        UserRequestGenerateCode userRequestGenerateCode = Fixture.createUserRequestGenerateCode(email);

        String errorMessage = "Houve um erro ao tentar gerar codigo para redefinir senha";
        errors.put("Email", "Email não foi encontrado");

        doThrow(new UserNotFoundException(errorMessage, errors)).when(validationService).analisarException(errorMessage, UserNotFoundException.class, errors);

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> usersService.gerarCodigoRedefinirSenha(userRequestGenerateCode, errors));

        assertThat(userNotFoundException.getMessage()).isEqualTo(errorMessage);
        assertThat(userNotFoundException.getFields().get("Email")).isEqualTo(errors.get("Email"));
    }

    @DisplayName("Deve retornar com sucesso mensagem de confirmacao de redefinicao de senha")
    @Test
    public void testVerificarCodigoRedefinirSenhaSucesso() {
        Long userId = 4L;
        String name = "davi";
        String email = "davi@gmail.com";
        String telefone = "+0000000-0000";
        String password = "senha123";
        Role role = Role.CLIENTE;
        int code = 12;

        String messageSucesso = name + " sua senha foi redefinida com sucesso";

        UserRequestGenerateNewPassword userRequestGenerateNewPassword = Fixture.createUserRequestGenerateNewPassword(email, password, code);
        CodigoVerificacaoEntity codigoVerificacaoEntity = Fixture.createCodigoVerificacaoEntity(1L, code);
        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        when(validationService.validarExistenciaEmail(userRequestGenerateNewPassword.getEmail(), errors)).thenReturn(usersEntity);
        when(validationService.validarCodigoVerificacao(userRequestGenerateNewPassword.getCode(), errors)).thenReturn(codigoVerificacaoEntity);
        when(passwordEncoder.encode(password)).thenReturn(password);

        String messageSucessoCompare = usersService.verificarCodigoRedefinirSenha(userRequestGenerateNewPassword, errors);

        assertThat(messageSucessoCompare).isEqualTo(messageSucesso);
    }

    @DisplayName("Deve retornar CodeNotValidException apos erro ao verificar codigo de verificacao")
    @Test
    public void testVerificarCodigoRedefinirSenhaError() {
        String email = "davi@gmail.com";
        String password = "senha123";
        int code = 12;

        UserRequestGenerateNewPassword userRequestGenerateNewPassword = Fixture.createUserRequestGenerateNewPassword(email, password, code);

        String errorMessage = "Houve um erro ao tentar redefinir senha";
        errors.put("Código", "Código de verificação inválido");
        errors.put("Email", "Email não foi encontrado");

        doThrow(new CodeNotValidException(errorMessage, errors)).when(validationService).analisarException(errorMessage, CodeNotValidException.class, errors);

        CodeNotValidException codeNotValidException = assertThrows(CodeNotValidException.class, () -> usersService.verificarCodigoRedefinirSenha(userRequestGenerateNewPassword, errors));

        assertThat(codeNotValidException.getMessage()).isEqualTo(errorMessage);
        assertThat(codeNotValidException.getFields().get("Código")).isEqualTo(errors.get("Código"));
        assertThat(codeNotValidException.getFields().get("Email")).isEqualTo(errors.get("Email"));
    }

    @DisplayName("Deve retornar com sucesso perfil de usuario")
    @Test
    public void testExibirPerfilSucesso() {
        Long userId = 34L;
        String name = "milena";
        String email = "milena@gmail.com";
        String telefone = "+0000000-0000";
        String password = "senha123";
        Role role = Role.CLIENTE;
        int code = 12;

        UsersEntity usersEntity = Fixture.createUsersEntity(userId, name, email, telefone, password, role, null, null, null, null);

        when(extractTypeUserContextHolder.extractUser()).thenReturn(usersEntity);
        when(resolverGeralUsers.viewProfile(any(UsersEntity.class))).thenReturn(usersEntity);

        UsersEntity usersEntityCompare = (UsersEntity) usersService.exibirPerfil();

        assertThat(usersEntityCompare.getId()).isEqualTo(usersEntity.getId());
        assertThat(usersEntityCompare.getName()).isEqualTo(usersEntity.getName());
        assertThat(usersEntityCompare.getEmail()).isEqualTo(usersEntity.getEmail());
        assertThat(usersEntityCompare.getTelefone()).isEqualTo(usersEntity.getTelefone());
        assertThat(usersEntityCompare.getPassword()).isEqualTo(usersEntity.getPassword());
        assertThat(usersEntityCompare.getRole()).isEqualTo(usersEntity.getRole());
    }
}
