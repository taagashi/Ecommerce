package br.com.thaua.Ecommerce.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminResponse {
    private Long id;
    private String name;
    private String email;
    private Integer contasBanidas;
    private LocalDateTime ultimoAcesso;
}
