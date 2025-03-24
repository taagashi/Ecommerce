package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;
//    PATCH /api/v1/clientes/cpf-telefone/update - atualizar cpf e telfone [ROLE: CLIENTE]
    @Operation(summary = "atualizar cpf e telefone", description = "o cliente autenticado adiciona/atualiza seu numero e cpf para pode realizar acoes mais ativas na aplicacao")
    @PatchMapping("cpf-telefone/update")
    public ResponseEntity<ClienteResponse> atualizarCpfETelefone(@RequestBody ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        return ResponseEntity.ok(clienteService.atualizarCpfETelefone(clienteCpfTelefoneRequest));
    }

//    GET /api/v1/clientes/view-profile - Exibir meu perfl [ROLE: CLIENTE]
    @Operation(summary = "exibir perfil", description = "cliente pode ver o seu perfil")
    @GetMapping("/view-profile")
    public ResponseEntity<ClienteComPedidoResponse> exibirPerfil() {
        return ResponseEntity.ok(clienteService.exibirPerfil());
    }

//    PUT /api/v1/clientes/update - Atualizar meus dados [ROLE: CLIENTE]1
    @Operation(summary = "atualizar dados", description = "cliente atualiza dados como: nome, email, telefone e cpf")
    @PutMapping("/update")
    public ResponseEntity<ClienteResponse> atualizarDados(@RequestBody ClienteUpdateRequest clienteUpdateRequest) {
        return ResponseEntity.ok(clienteService.atualizarDados(clienteUpdateRequest));
    }
}
