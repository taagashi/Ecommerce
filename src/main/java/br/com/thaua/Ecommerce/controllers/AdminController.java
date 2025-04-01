package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Listar clientes", description = "Lista todos os clientes")
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        return ResponseEntity.ok(adminService.listarClientes());
    }

    @Operation(summary = "listar fornecedores", description = "lista todos os fornecedores")
    @GetMapping("/fornecedores")
    public ResponseEntity<List<FornecedorResponse>> listarFornecedores() {
        return ResponseEntity.ok(adminService.listarFornecedores());
    }

//    POST /api/v1/categorias/register - Cadastrar nova categoria [ROLE ADMIN]
    @Operation(summary = "cadastrar nova categoria", description = "admin pode cadastrar novas categorias para produtos")
    @PostMapping("/categorias/register")
    public ResponseEntity<CategoriaResponse> cadastrarNovaCategoria(@RequestBody CategoriaRequest categoriaRequest) {
        return ResponseEntity.ok(adminService.cadastrarNovaCategoria(categoriaRequest));
    }

//    DELETE /api/v1/clientes/{id} - Remover cliente [ROLE: ADMIN]
    @Operation(summary = "remover cliente", description = "admin pode remover a conta de um cliente atraves do id do cliente")
    @DeleteMapping("/clientes/{clienteId}")
    public ResponseEntity<String> removerCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(adminService.removerCliente(clienteId));
    }

//   GET /api/v1/clientes/{id}/pedidos - Listar pedidos do cliente [ROLE: ADMIN]
    @Operation(summary = "listar pedidos de um cliente", description = "admin pode listar os pedidos de um determinado cliente atraves do id do cliente")
    @GetMapping("/clientes/{clienteId}/pedidos/list")
    public ResponseEntity<List<PedidoResponse>> listarPedidosDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(adminService.listarPedidosDoCliente(clienteId));
    }

//    PATCH /api/v1/pedidos/{id}/status/update - Atualizar status do pedido [ROLE: ADMIN
    @Operation(summary = "atualizar status do pedido", description = "admin pode atualizar o status de um pedido atraves do id do pedido")
    @PatchMapping("/pedidos/{pedidoId}/status/update")
    public ResponseEntity<PedidoResponse> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoPatchRequest pedidoPatchRequest) {
        return ResponseEntity.ok(adminService.atualizarStatusPedido(pedidoId, pedidoPatchRequest));
    }
}
