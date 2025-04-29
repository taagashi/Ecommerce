package br.com.thaua.Ecommerce.dto.fornecedor;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FornecedorViewProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cnpj;
    private BigDecimal saldo;
    private Integer produtosEnviados;
    private Integer produtosCadastrados;
}
