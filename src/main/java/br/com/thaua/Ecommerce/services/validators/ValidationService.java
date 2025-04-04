package br.com.thaua.Ecommerce.services.validators;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ValidationService {

    public void validarCadastroEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if (usersEntity.getEndereco() != null) {
            errors.put("Endereço", "Endereço já cadastrado");
        }
    }

    public void validarSiglaEstado(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        if(Arrays.stream(Estado.values()).noneMatch(estado -> estado.toString().equals(enderecoRequest.getEstado()))) {
            errors.put("Estado", "Sigla de estado inválido");
        }
    }

    public void validarEnderecoNaoExistente(UsersEntity usersEntity, Map<String, String> errors) {
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
        }
    }


    public void validarTelefone(UsersEntity usersEntity, Map<String, String> errors) {
        if (usersEntity.getTelefone() == null) {
            errors.put("Telefone", "Telefone não cadastrado");
        }
    }

    public void validarCNPJ(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getFornecedor().getCnpj() == null) {
            errors.put("CNPJ", "CNPJ está não cadastrado");
        }
    }

    public void validarEstoque(ProdutoRequest produtoRequest, Map<String, String> errors) {
        if(produtoRequest.getEstoque() <= 0) {
            errors.put("Estoque", "Não é possível um produto com estoque zerado ou inferior");
        }
    }

    public void validarPreco(ProdutoRequest produtoRequest, Map<String, String> errors) {
        errors.put("Preço", "Não é possivel cadastrar um produto com preço zerado ou inferior");
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
