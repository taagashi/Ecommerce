package br.com.thaua.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cpf;
}
