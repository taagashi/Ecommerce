package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
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

//    PATCH /api/v1/pedidos/{id}/status/    update - Atualizar status do pedido [ROLE: ADMIN
    @Operation(summary = "atualizar status do pedido", description = "admin pode atualizar o status de um pedido atraves do id do pedido")
    @PatchMapping("/pedidos/{pedidoId}/status/update")
    public ResponseEntity<PedidoResponse> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoPatchRequest pedidoPatchRequest) {
        return ResponseEntity.ok(adminService.atualizarStatusPedido(pedidoId, pedidoPatchRequest));
    }

//    POST /api/v1/users/{userId}/enderecos/register - Cadastrar endereço para Usuarios [ROLE: ADMIN]
    @Operation(summary = "cadastrar endereco para um usuario", description = "admin pode cadastrar endereco para um usuario especifico")
    @PostMapping("/users/{userId}/enderecos/register")
    public ResponseEntity<EnderecoResponse> cadastrarEnderecoUsuario(@PathVariable Long userId, EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(adminService.cadastrarEnderecoUsuario(userId, enderecoRequest));
    }

//    GET /api/v1/users/{userId}/enderecos/list - Exibir endereco do usuario [ROLE: ADMIN]
    @Operation(summary = "exibir endereco do usuario", description = "admin pode exibir o endereco de um usuario especifico")
    @GetMapping("/users/{userId}/endereco/list")
    public ResponseEntity<EnderecoResponse> exibirEnderecoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.exibirEnderecoUsuario(userId));
    }

//    PUT /api/users/{userId}/enderecos/update - Atualizar endereço do usuario [ROLE: ADMIN]
    @Operation(summary = "atualizar endereco do usuario", description = "admin pode atualizar endereco de um usuario especifico")
    @PutMapping("/users/{userId}/enderecos/update")
    public ResponseEntity<EnderecoResponse> atualizarEnderecoUsuario(@PathVariable Long userId, @RequestBody EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(adminService.atualizarEnderecoUsuario(userId, enderecoRequest));
    }

//    DELETE /api/v1/users/{userId}/enderecos/delete` - Remover endereço do usuario [ROLE: ADMIN]
    @Operation(summary = "remover endereco do usuario", description = "admin pode remover o endereco de um usuario especifico")
    @DeleteMapping("/users/{userId}/enderecos/delete")
    public ResponseEntity<String> deletarEnderecoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deletarEnderecoUsuario(userId));
    }

//    PUT /api/V1/categorias/update - Atualizar categoria [ROLE ADMIN]
    @Operation(summary = "atualizar categoria", description = "admin pode atualizar as informacoes de uma categoria especifica")
    @PutMapping("/categorias/{categoriaId}/update")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaRequest categoriaRequest) {
        return ResponseEntity.ok(adminService.atualizarCategoria(categoriaId, categoriaRequest));
    }
}
