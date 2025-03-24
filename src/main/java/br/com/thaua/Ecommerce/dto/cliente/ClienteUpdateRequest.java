package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUpdateRequest {
    private String name;
    private String email;
    private String telefone;
    private String cpf;
}
