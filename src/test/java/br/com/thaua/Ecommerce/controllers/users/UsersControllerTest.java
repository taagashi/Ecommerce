package br.com.thaua.Ecommerce.controllers.users;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.UsersController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.exceptions.CodeNotValidException;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.services.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {
    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Map<String, String> emptyMap;

    private Map<String, String> errors;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .build();
        objectMapper = new ObjectMapper();
        errors = ConstructorErrors.returnMapErrors();
        emptyMap = ConstructorErrors.returnMapErrors();
    }

    @DisplayName("Deve retornar com sucesso token ao final do cadastro")
    @Test
    public void testCadastroSucesso() throws Exception{
        UsersRequest usersRequest = Fixture.createUserRequest("taagashi", "taagashi.dev@gmail.com", "tagashis", "ADMIN");
       String jwtToken = Fixture.createJwtToken();

        String usersRequestJson = objectMapper.writeValueAsString(usersRequest);

        when(usersService.cadastrarUsuario(eq(usersRequest))).thenReturn(jwtToken);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usersRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(jwtToken));

        verify(usersService, times(1)).cadastrarUsuario(eq(usersRequest));
    }

    @DisplayName("Deve retornar com sucesso mensagem ao deletar conta")
    @Test
    public void testDeletarContaSucesso() throws Exception {
        String mensagemDelecao= "usuario, sua conta foi deletada com sucesso";

        when(usersService.deletarConta()).thenReturn(mensagemDelecao);

        mockMvc.perform(delete("/api/v1/users/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string(mensagemDelecao));

        verify(usersService, times(1)).deletarConta();
    }

    @DisplayName("Deve retornar com sucesso mensagem ao gerar codigo de verificacao")
    @Test
    public void testGerarCodigoVerificacaoSucesso() throws Exception {
        UserRequestGenerateCode userRequestGenerateCode = Fixture.createUserRequestGenerateCode("taagashi.dev@gmail.com");
        String menssagemCodigoEnviado = "usuario, foi enviado um codigo de verificação para " + userRequestGenerateCode.getEmail();

        String userRequestGenerateCodeJson = objectMapper.writeValueAsString(userRequestGenerateCode);

        when(usersService.gerarCodigoRedefinirSenha(eq(userRequestGenerateCode), eq(emptyMap))).thenReturn(menssagemCodigoEnviado);

        mockMvc.perform(post("/api/v1/users/gerar-codigo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestGenerateCodeJson))
                .andExpect(status().isOk())
                .andExpect(content().string(menssagemCodigoEnviado));

        verify(usersService, times(1)).gerarCodigoRedefinirSenha(eq(userRequestGenerateCode), eq(emptyMap));
    }

    @DisplayName("Deve retornar um UserNotFoundException ao tentar gerar codigo de verificacao")
    @Test
    public void testGerarCodigoVerificacaoError() throws Exception {
        UserRequestGenerateCode userRequestGenerateCode = Fixture.createUserRequestGenerateCode("tomas@gmail.com");
        errors.put("Email", "Email não foi encontrado");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar gerar codigo para redefinir senha");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, errors);

        String userRequestGenerateCodeJson = objectMapper.writeValueAsString(userRequestGenerateCode);

        when(usersService.gerarCodigoRedefinirSenha(eq(userRequestGenerateCode), eq(emptyMap))).thenThrow(userNotFoundException);

        mockMvc.perform(post("/api/v1/users/gerar-codigo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestGenerateCodeJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Email']").value(errors.get("Email")));

        verify(usersService, times(1)).gerarCodigoRedefinirSenha(eq(userRequestGenerateCode), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso mensagem ao redefinir senha")
    @Test
    public void testRedefinirSenhaSucesso() throws Exception {
        UserRequestGenerateNewPassword userRequestGenerateNewPassword = Fixture.createUserRequestGenerateNewPassword("Lucas@gmail.com", "lucas123", 12332);
        int code = userRequestGenerateNewPassword.getCode();
        String mensagemRedefinirSenhaSucesso = "usuario sua senha foi redefinida com sucesso";

        String userRequestGenerateNewPasswordJson = objectMapper.writeValueAsString(userRequestGenerateNewPassword);

        when(usersService.verificarCodigoRedefinirSenha(eq(userRequestGenerateNewPassword), eq(emptyMap))).thenReturn(mensagemRedefinirSenhaSucesso);

        mockMvc.perform(patch("/api/v1/users/redefinir-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestGenerateNewPasswordJson))
                .andExpect(status().isOk())
                .andExpect(content().string(mensagemRedefinirSenhaSucesso));

        verify(usersService, times(1)).verificarCodigoRedefinirSenha(eq(userRequestGenerateNewPassword), eq(emptyMap));
    }

    @DisplayName("Deve retornar um CodeNotValidException ao tentar redefinir senha")
    @Test
    public void testRedefinirSenhaError() throws Exception {
        UserRequestGenerateNewPassword userRequestGenerateNewPassword = Fixture.createUserRequestGenerateNewPassword("email errado", "23213", 13302);
        errors.put("Email", "Email não foi encontrado");
        errors.put("Código", "Código de verificação inválido");
        String errorMessage = Fixture.createErrorMessage("Houve um erro ao tentar redefinir senha");
        CodeNotValidException codeNotValidException = new CodeNotValidException(errorMessage, errors);

        String userRequestGenerateNewPasswordJson = objectMapper.writeValueAsString(userRequestGenerateNewPassword);

        when(usersService.verificarCodigoRedefinirSenha(eq(userRequestGenerateNewPassword), eq(emptyMap))).thenThrow(codeNotValidException);

        mockMvc.perform(patch("/api/v1/users/redefinir-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequestGenerateNewPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Email']").value(errors.get("Email")))
                .andExpect(jsonPath("$.fieldsErrors['Código']").value(errors.get("Código")));

        verify(usersService, times(1)).verificarCodigoRedefinirSenha(eq(userRequestGenerateNewPassword), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso os dados do usuario ao exibir perfil")
    @Test
    public void testExibirPerfilSucesso() throws Exception {
        AdminResponse adminResponse = Fixture.createAdminResponse(1L, "taagashi", "taagashi.dev@gmail.com", 15, LocalDateTime.now());

        when(usersService.exibirPerfil()).thenReturn(adminResponse);

        mockMvc.perform(get("/api/v1/users/exibir-perfil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.contasBanidas").value(adminResponse.getContasBanidas()))
                .andExpect(jsonPath("$.ultimoAcesso[0]").value(adminResponse.getUltimoAcesso().getYear()))
                .andExpect(jsonPath("$.ultimoAcesso[1]").value(adminResponse.getUltimoAcesso().getMonthValue()))
                .andExpect(jsonPath("$.ultimoAcesso[2]").value(adminResponse.getUltimoAcesso().getDayOfMonth()))
                .andExpect(jsonPath("$.ultimoAcesso[3]").value(adminResponse.getUltimoAcesso().getHour()))
                .andExpect(jsonPath("$.ultimoAcesso[4]").value(adminResponse.getUltimoAcesso().getMinute()))
                .andExpect(jsonPath("$.ultimoAcesso[5]").value(adminResponse.getUltimoAcesso().getSecond()))
                .andExpect(jsonPath("$.ultimoAcesso[6]").value(adminResponse.getUltimoAcesso().getNano()));

        verify(usersService, times(1)).exibirPerfil();

    }
}
