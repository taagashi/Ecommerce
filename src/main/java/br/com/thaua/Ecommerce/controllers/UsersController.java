package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.domain.entity.Users;
import br.com.thaua.Ecommerce.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService userService;

    @PostMapping("/register")
    private Users testeCadastro(@RequestBody Users users) {
        return userService.cadastrarUsuario(users);
    }
}
