package br.com.thaua.Ecommerce.dto.fornecedor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cnpj;
}
