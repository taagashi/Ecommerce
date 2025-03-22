package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.ClienteResponse;
import br.com.thaua.Ecommerce.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Listar clientes", description = "Lista todos os clientes", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        return ResponseEntity.ok(adminService.listarClientes());
    }
}
