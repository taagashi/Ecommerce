package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Data;

@Data
public class ClienteUpdateRequest {
    private String name;
    private String email;
    private String telefone;
    private String cpf;
}
