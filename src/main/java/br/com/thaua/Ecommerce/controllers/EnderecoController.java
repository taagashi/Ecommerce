package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import br.com.thaua.Ecommerce.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users/enderecos")
public class EnderecoController {
//    POST /api/v1/users/enderecos/register - Cadastrar endereco [USUARIO AUTENTICADO]
    private final UsersService usersService;
    private final EnderecoMapper enderecoMapper;

    @PostMapping("/register")
    public ResponseEntity<EnderecoResponse> cadastrarEndereco(@RequestBody EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(usersService.cadastrarEndereco(enderecoRequest));
    }
}
