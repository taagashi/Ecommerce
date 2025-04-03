package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.exceptions.AddressException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    public void verificarEnderecoExistente(UsersEntity usersEntity) {
        if (usersEntity.getEndereco() != null) {
            throw new AddressException(usersEntity.getName() + ", você já tem um endereço cadastrado");
        }
    }

}
