package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.UsersRequest;
import br.com.thaua.Ecommerce.dto.UsersResponse;
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

//    LEMBRAR DE MUDAR O USERSREQUEST
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersRequest usersRequest) {
        return null;
    }
}
