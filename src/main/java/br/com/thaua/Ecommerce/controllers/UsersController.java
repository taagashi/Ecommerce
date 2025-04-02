package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.users.UsersLoginRequest;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.dto.users.UsersResponse;
import br.com.thaua.Ecommerce.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService userService;

    @Operation(summary = "Cadastrar usuario", description = "necessario passar dados como nome, email e senha")
    @PostMapping("/register")
    public ResponseEntity<String> cadastro(@RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(userService.cadastrarUsuario(usersRequest));
    }

    @Operation(summary = "Login", description = "necessario passar dados como email e senha")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersLoginRequest usersLoginRequest) {
        return ResponseEntity.ok(userService.login(usersLoginRequest.getEmail(), usersLoginRequest.getPassword()));
    }

    @Operation(summary = "Deletar conta", description = "O usuario autenticado pode deletar a conta dele", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletarConta() {
        return ResponseEntity.ok(userService.deletarConta());
    }
}
