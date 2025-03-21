package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.UsersLoginRequest;
import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
import br.com.thaua.Ecommerce.services.JWTService;
import br.com.thaua.Ecommerce.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService userService;

    @PostMapping("/register")
    public ResponseEntity<UsersResponse> testeCadastro(@RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(userService.cadastrarUsuario(usersRequest));
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersLoginRequest usersLoginRequest) {
        return ResponseEntity.ok(userService.login(usersLoginRequest.getEmail(), usersLoginRequest.getPassword()));
    }
}
