package br.com.thaua.Ecommerce.dto.fornecedor;

import lombok.Data;

@Data
public class FornecedorResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cnpj;
}
