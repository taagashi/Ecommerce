package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.EnderecoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.exceptions.AddressException;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidationService {
    public void validarEnderecoExistente(UsersEntity usersEntity) {
        if (usersEntity.getEndereco() != null) {
            throw new AddressException(usersEntity.getName() + ", você já tem um endereço cadastrado no sistema. Tente atualizar ou limpar informações", Map.of("endereco", "Endereço já cadastrado"));
        }
    }

    public void validarSiglaEstado(EnderecoMapper enderecoMapper, EnderecoRequest enderecoRequest, UsersEntity usersEntity) {
        try {
            EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        }catch(IllegalArgumentException e) {
            throw new AddressException(usersEntity.getName() + " você precisa adicionar uma sigla de estado que seja válida", Map.of("estado", "Estado inválido"));
        }
    }

    public void validarExibicaoEndereco(UsersEntity usersEntity) {
        if(usersEntity.getEndereco() == null) {
            throw new AddressException(usersEntity.getName() + ", você ainda não cadastrou um endereço", Map.of("endereco", "Endereço não cadastrado"));
        }
    }
}
