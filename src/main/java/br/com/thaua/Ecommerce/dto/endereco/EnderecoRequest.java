package br.com.thaua.Ecommerce.dto.endereco;

import lombok.Data;

@Data
public class EnderecoRequest {
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
