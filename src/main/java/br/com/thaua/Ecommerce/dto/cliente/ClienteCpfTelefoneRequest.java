package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCpfTelefoneRequest {
    private String cpf;
    private String telefone;
}
