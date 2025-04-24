package br.com.thaua.Ecommerce.dto.fornecedor;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
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
