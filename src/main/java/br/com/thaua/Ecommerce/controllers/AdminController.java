package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.domain.entity.FornecedorEntity;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Listar clientes", description = "Lista todos os clientes")
    @GetMapping("/clientes")
    public ResponseEntity<Pagina<ClienteResponse>> listarClientes(Pageable pageable) {
        return ResponseEntity.ok(adminService.listarClientes(pageable));
    }

    @Operation(summary = "buscar cliente", description = "admin pode buscar um cliente especifico")
    @GetMapping("/clientes/{clienteId}/list")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(adminService.buscarCliente(clienteId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "buscar fornecedor", description = "admin pode buscar um fornecedor especifico")
    @GetMapping("/fornecedores/{fornecedorId}/list")
    public ResponseEntity<FornecedorResponse> buscarFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(adminService.buscarFornecedor(fornecedorId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "listar admin's", description = "admin pode ver todos os admin's registrados")
    @GetMapping("/list")
    public ResponseEntity<Pagina<AdminResponse>> listarAdmins(Pageable pageable) {
        return ResponseEntity.ok(adminService.listarAdmins(pageable));
    }

    @Operation(summary = "buscar admin", description = "admin pode buscar por outro admin especifico")
    @GetMapping("/{adminId}/list")
    public ResponseEntity<AdminResponse> buscarAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.buscarAdmin(adminId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "listar fornecedores", description = "lista todos os fornecedores")
    @GetMapping("/fornecedores/list")
    public ResponseEntity<Pagina<FornecedorResponse>> listarFornecedores(Pageable pageable) {
        return ResponseEntity.ok(adminService.listarFornecedores(pageable));
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
        return ResponseEntity.ok(adminService.removerCliente(clienteId, ConstructorErrors.returnMapErrors()));
    }

//   GET /api/v1/clientes/{id}/pedidos - Listar pedidos do cliente [ROLE: ADMIN]
    @Operation(summary = "listar pedidos de um cliente", description = "admin pode listar os pedidos de um determinado cliente atraves do id do cliente")
    @GetMapping("/clientes/{clienteId}/pedidos/list")
    public ResponseEntity<Pagina<PedidoResponse>> listarPedidosDoCliente(@PathVariable Long clienteId, Pageable pageable) {
        return ResponseEntity.ok(adminService.listarPedidosDoCliente(clienteId, pageable, ConstructorErrors.returnMapErrors()));
    }

//    PATCH /api/v1/pedidos/{id}/status/    update - Atualizar status do pedido [ROLE: ADMIN
    @Operation(summary = "atualizar status do pedido", description = "admin pode atualizar o status de um pedido atraves do id do pedido")
    @PatchMapping("/pedidos/{pedidoId}/status/update")
    public ResponseEntity<PedidoResponse> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoPatchRequest pedidoPatchRequest) {
        return ResponseEntity.ok(adminService.atualizarStatusPedido(pedidoId, pedidoPatchRequest, ConstructorErrors.returnMapErrors()));
    }

//    POST /api/v1/users/{userId}/enderecos/register - Cadastrar endereço para Usuarios [ROLE: ADMIN]
    @Operation(summary = "cadastrar endereco para um usuario", description = "admin pode cadastrar endereco para um usuario especifico")
    @PostMapping("/users/{userId}/enderecos/register")
    public ResponseEntity<EnderecoResponse> cadastrarEnderecoUsuario(@PathVariable Long userId, EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(adminService.cadastrarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

//    GET /api/v1/users/{userId}/enderecos/list - Exibir endereco do usuario [ROLE: ADMIN]
    @Operation(summary = "exibir endereco do usuario", description = "admin pode exibir o endereco de um usuario especifico")
    @GetMapping("/users/{userId}/endereco/list")
    public ResponseEntity<EnderecoResponse> exibirEnderecoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.exibirEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

//    PUT /api/users/{userId}/enderecos/update - Atualizar endereço do usuario [ROLE: ADMIN]
    @Operation(summary = "atualizar endereco do usuario", description = "admin pode atualizar endereco de um usuario especifico")
    @PutMapping("/users/{userId}/enderecos/update")
    public ResponseEntity<EnderecoResponse> atualizarEnderecoUsuario(@PathVariable Long userId, @RequestBody EnderecoRequest enderecoRequest) {
        return ResponseEntity.ok(adminService.atualizarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

//    DELETE /api/v1/users/{userId}/enderecos/delete` - Remover endereço do usuario [ROLE: ADMIN]
    @Operation(summary = "remover endereco do usuario", description = "admin pode remover o endereco de um usuario especifico")
    @DeleteMapping("/users/{userId}/enderecos/delete")
    public ResponseEntity<String> deletarEnderecoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deletarEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

//    PUT /api/V1/categorias/update - Atualizar categoria [ROLE ADMIN]
    @Operation(summary = "atualizar categoria", description = "admin pode atualizar as informacoes de uma categoria especifica")
    @PutMapping("/categorias/{categoriaId}/update")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaRequest categoriaRequest) {
        return ResponseEntity.ok(adminService.atualizarCategoria(categoriaId, categoriaRequest, ConstructorErrors.returnMapErrors()));
    }

//    DELETE /api/categorias/{id}/delete` - Remover categoria [ROLE ADMIN]
    @Operation(summary = "deletar categoria", description = "admin pode deletar uma categoria especifica")
    @DeleteMapping("/categorias/{categoriaId}/delete")
    public ResponseEntity<String> deletarCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(adminService.deletarCategoria(categoriaId, ConstructorErrors.returnMapErrors()));
    }
}
