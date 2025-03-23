package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;
//    PATCH /api/v1/clientes/cpf-telefone/update - atualizar cpf e telfone [ROLE: CLIENTE]
    @PatchMapping("cpf-telefone/update")
    public ResponseEntity<ClienteResponse> atualizarCpfETelefone(@RequestBody ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        return ResponseEntity.ok(clienteService.atualizarCpfETelefone(clienteCpfTelefoneRequest));
    }
}
