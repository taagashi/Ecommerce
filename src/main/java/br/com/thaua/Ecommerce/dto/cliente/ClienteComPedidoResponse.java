package br.com.thaua.Ecommerce.dto.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteComPedidoResponse {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cpf;
    private Integer pedidosFeitos;
}
