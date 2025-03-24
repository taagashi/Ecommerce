package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.mappers.EnderecoMapper;
import br.com.thaua.Ecommerce.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users/enderecos")
public class EnderecoController {
//    POST /api/v1/users/enderecos/register - Cadastrar endereco [USUARIO AUTENTICADO]
    private final UsersService usersService;
    private final EnderecoMapper enderecoMapper;

    @Operation(summary = "cadastrar endereco", description = "o usuario autenticado pode cadastrar um endereco para si")
    @PostMapping("/register")
    public ResponseEntity<EnderecoResponse> cadastrarEndereco(@RequestBody EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(usersService.cadastrarEndereco(enderecoRequest));
    }

//    GET /api/v1/users/enderecos/list - Exibir meu endereco [USUARIO AUTENTICADO]
    @Operation(summary = "Exibir endereco", description = "usuario cadastrado pode ver o seu endereco completo")
    @GetMapping("/list")
    public ResponseEntity<EnderecoResponse> exibirEndereco() {
        return ResponseEntity.ok(usersService.exibirEndereco());
    }
}
