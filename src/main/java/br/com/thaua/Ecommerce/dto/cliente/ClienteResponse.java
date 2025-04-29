package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Data;

@Data
public class ClienteResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cpf;
}
