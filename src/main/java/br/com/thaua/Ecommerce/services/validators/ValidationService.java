package br.com.thaua.Ecommerce.services.validators;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.exceptions.AddressException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ValidationService {

    public void validarEnderecoExistente(UsersEntity usersEntity, Map<String, String> errors) {
        if (usersEntity.getEndereco() != null) {
            errors.put("Endereço", "Endereço já cadastrado");
        }
    }

    public void validarSiglaEstado(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        if(Arrays.stream(Estado.values()).noneMatch(estado -> estado.toString().equals(enderecoRequest.getEstado()))) {
            errors.put("Estado", "Sigla de estado inválido");
        }
    }

    public void validarExibicaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarDelecaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarAtualizacaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
            throw new AddressException(usersEntity.getName() + " não é possível atualizar seu endereço porque você ainda não tem um", Map.of("endereco", "Endereço não cadastrado"));
        }
    }

    public void analisarException(String message, Class<? extends RuntimeException> typeException, Map<String, String> errors) {
        if(!errors.isEmpty()) {
            try {
                Constructor<? extends RuntimeException> constructor = typeException.getConstructor(String.class, Map.class);

                throw constructor.newInstance(message, errors);

            }catch (ReflectiveOperationException e) {
                throw new RuntimeException("Erro ao lançar exceção personalizada", e);
            }
        }
    }
}
