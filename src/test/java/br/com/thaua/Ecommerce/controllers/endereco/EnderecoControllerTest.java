package br.com.thaua.Ecommerce.controllers.endereco;

import br.com.thaua.Ecommerce.Fixture;
import br.com.thaua.Ecommerce.controllers.EnderecoController;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.controllers.handler.ExceptionHandlerClass;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.exceptions.AtualizarEnderecoException;
import br.com.thaua.Ecommerce.exceptions.CadastroEnderecoException;
import br.com.thaua.Ecommerce.exceptions.EnderecoNaoExistenteException;
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

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class EnderecoControllerTest {
    @InjectMocks
    private EnderecoController enderecoController;

    @Mock
    private UsersService usersService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Map<String, String> errors;

    private Map<String, String> emptyMap;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(enderecoController)
                .setControllerAdvice(new ExceptionHandlerClass())
                .build();
        errors = ConstructorErrors.returnMapErrors();
        emptyMap = ConstructorErrors.returnMapErrors();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Deve retornar com sucesso um endereço cadastrado")
    @Test
    public void testCadastrarEnderecoSucesso() throws Exception {
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Mariano de Brito", "79", "Centro", "Cidade", "RN", "48323341");
        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(1L, "taagashi", enderecoRequest.getRua(), enderecoRequest.getNumero(), enderecoRequest.getBairro(), enderecoRequest.getCidade(), enderecoRequest.getEstado(), enderecoRequest.getCep());

        when(usersService.cadastrarEndereco(eq(enderecoRequest), eq(emptyMap))).thenReturn(enderecoResponse);

        mockMvc.perform(post("/api/v1/users/enderecos/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.cep").value(enderecoResponse.getCep()));

        verify(usersService, times(1)).cadastrarEndereco(eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar com erro CadastroEnderecoException ao tentar cadastrar um endereço")
    @Test
    public void testCadastrarEnderecoError() throws Exception{
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Rua Mariano de Brito", "79", "Centro", "Cidade", "Estado errado", "48323341");
        String errorMessage = Fixture.createErrorMessage("usuario, houve um erro no cadastrado do endereco");
        errors.put("Endereço", "Endereço já cadastrado");
        errors.put("Estado", "Sigla de estado inválido");

        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);
        CadastroEnderecoException cadastroEnderecoException = new CadastroEnderecoException(errorMessage, errors);

        when(usersService.cadastrarEndereco(eq(enderecoRequest), eq(emptyMap))).thenThrow(cadastroEnderecoException);

        mockMvc.perform(post("/api/v1/users/enderecos/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Endereço']").value(errors.get("Endereço")))
                .andExpect(jsonPath("$.fieldsErrors['Estado']").value(errors.get("Estado")));

        verify(usersService, times(1)).cadastrarEndereco(eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar com sucesso um endereço exibido")
    @Test
    public void testExibirEnderecoSucesso() throws Exception {
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(2L, "taagashi", "Rua Mariano de Brito", "79", "Centro", "Cidade", "RN", "4832334");
        Map<String, String> errors = ConstructorErrors.returnMapErrors();

        when(usersService.exibirEndereco(eq(errors))).thenReturn(enderecoResponse);

        mockMvc.perform(get("/api/v1/users/enderecos/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()));

        verify(usersService, times(1))
                .exibirEndereco(eq(errors));
    }

    @DisplayName("Deve remover com sucesso um endereço")
    @Test
    public void testRemoverEnderecoSucesso() throws Exception {

        mockMvc.perform(delete("/api/v1/users/enderecos/delete"))
                .andExpect(status().isOk());

        verify(usersService, times(1)).deletarEndereco(eq(emptyMap));
    }

    @DisplayName("Deve retornar com erro EnderecoNaoExistenteException ao tentar remover um endereço")
    @Test
    public void testRemoverEnderecoError() throws Exception {
        String errorMessage = Fixture.createErrorMessage("usuario você não pode limpar as informações do seu endereco porque ainda não adicionou um");
        errors.put("Endereço", "Endereço não cadastrado");
        EnderecoNaoExistenteException enderecoNaoExistenteException = new EnderecoNaoExistenteException(errorMessage, errors);

        when(usersService.deletarEndereco(eq(emptyMap))).thenThrow(enderecoNaoExistenteException);

        mockMvc.perform(delete("/api/v1/users/enderecos/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Endereço']").value(errors.get("Endereço")));

        verify(usersService, times(1)).deletarEndereco(eq(emptyMap));
    }

    @DisplayName("Deve atualizar com sucesso um endereço")
    @Test
    public void testAtualizarEnderecoSucesso() throws Exception {
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Joao", "32", "nova", "São Paulo", "SP", "2233333");
        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);
        EnderecoResponse enderecoResponse = Fixture.createEnderecoResponse(9L, "Thiago Nunes", enderecoRequest.getRua(), enderecoRequest.getNumero(), enderecoRequest.getBairro(), enderecoRequest.getCidade(), enderecoRequest.getEstado(), enderecoRequest.getCep());

        when(usersService.atualizarEndereco(eq(enderecoRequest), eq(emptyMap))).thenReturn(enderecoResponse);

        mockMvc.perform(put("/api/v1/users/enderecos/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEndereco").value(enderecoResponse.getIdEndereco()))
                .andExpect(jsonPath("$.nameUser").value(enderecoResponse.getNameUser()))
                .andExpect(jsonPath("$.rua").value(enderecoResponse.getRua()))
                .andExpect(jsonPath("$.numero").value(enderecoResponse.getNumero()))
                .andExpect(jsonPath("$.bairro").value(enderecoResponse.getBairro()))
                .andExpect(jsonPath("$.cidade").value(enderecoResponse.getCidade()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()))
                .andExpect(jsonPath("$.estado").value(enderecoResponse.getEstado()));

        verify(usersService, times(1)).atualizarEndereco(eq(enderecoRequest), eq(emptyMap));
    }

    @DisplayName("Deve retornar com erro AtualizarEnderecoException ao tentar atualizar um endereço")
    @Test
    public void testAtualizarEnderecoError() throws Exception {
        EnderecoRequest enderecoRequest = Fixture.createEnderecoRequest("Manu", "50", "Santo Antonio", "Rio de Janeiro", "Estado errado", "13123");
        String enderecoRequestJson = objectMapper.writeValueAsString(enderecoRequest);
        errors.put("Endereço", "Endereço não cadastrado");
        errors.put("Estado", "Sigla de estado inválido");

        String errorMessage = Fixture.createErrorMessage("usuario, houve um erro na atualização do seu endereço");
        AtualizarEnderecoException atualizarEnderecoException = new AtualizarEnderecoException(errorMessage, errors);


        when(usersService.atualizarEndereco(eq(enderecoRequest), eq(emptyMap))).thenThrow(atualizarEnderecoException);

        mockMvc.perform(put("/api/v1/users/enderecos/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(enderecoRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.fieldsErrors").isMap())
                .andExpect(jsonPath("$.fieldsErrors['Endereço']").value(errors.get("Endereço")))
                .andExpect(jsonPath("$.fieldsErrors['Estado']").value(errors.get("Estado")));

        verify(usersService, times(1)).atualizarEndereco(eq(enderecoRequest), eq(emptyMap));
    }
}
