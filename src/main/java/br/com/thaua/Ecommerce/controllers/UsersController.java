package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import br.com.thaua.Ecommerce.dto.users.UsersLoginRequest;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
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

    @Operation(summary = "gerar codigo de verificao", description = "usuario nao autenticado pode gerar um codigo de vericacao para poder mudar sua senha, tudo a partir de seu email")
    @PostMapping("/gerar-codigo")
    public ResponseEntity<String> gerarCodigoVerificacao(@RequestBody UserRequestGenerateCode userRequestGenerateCode) {
        return ResponseEntity.ok(userService.gerarCodigoRedefinirSenha(userRequestGenerateCode, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "redefinir senha", description = "usuario nao autenticado pode redefinir sua senha atraves do codigo de verificacao")
    @PatchMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody UserRequestGenerateNewPassword userRequestGenerateNewPassword) {
        return ResponseEntity.ok(userService.verificarCodigoRedefirnirSenha(userRequestGenerateNewPassword, ConstructorErrors.returnMapErrors()));
    }
}
