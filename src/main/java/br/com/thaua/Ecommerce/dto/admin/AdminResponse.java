package br.com.thaua.Ecommerce.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminResponse {
    private Long id;
    private String name;
    private String email;
    private Integer contasBanidas;
    private LocalDateTime ultimoAcesso;
}
