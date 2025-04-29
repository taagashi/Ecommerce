package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Data;

@Data
public class ClienteCpfTelefoneRequest {
    private String cpf;
    private String telefone;
}
