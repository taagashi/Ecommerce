package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @Cacheable("clientes")
    @Operation(summary = "Listar clientes", description = "Lista todos os clientes")
    @GetMapping("/clientes")
    public ResponseEntity<Pagina<ClienteResponse>> listarClientes(Pageable pageable) {
        log.info("listar clientes");
        return ResponseEntity.ok(adminService.listarClientes(pageable));
    }

    @Cacheable("clientes")
    @Operation(summary = "buscar cliente", description = "admin pode buscar um cliente especifico")
    @GetMapping("/clientes/{clienteId}/list")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable Long clienteId) {
        log.info("buscar cliente");
        return ResponseEntity.ok(adminService.buscarCliente(clienteId, ConstructorErrors.returnMapErrors()));
    }

    @Cacheable("fornecedores")
    @Operation(summary = "buscar fornecedor", description = "admin pode buscar um fornecedor especifico")
    @GetMapping("/fornecedores/{fornecedorId}/list")
    public ResponseEntity<FornecedorResponse> buscarFornecedor(@PathVariable Long fornecedorId) {
        log.info("buscar fornecedor");
        return ResponseEntity.ok(adminService.buscarFornecedor(fornecedorId, ConstructorErrors.returnMapErrors()));
    }

    @Cacheable("admins")
    @Operation(summary = "listar admin's", description = "admin pode ver todos os admin's registrados")
    @GetMapping("/list")
    public ResponseEntity<Pagina<AdminResponse>> listarAdmins(Pageable pageable) {
        log.info("listar admins");
        return ResponseEntity.ok(adminService.listarAdmins(pageable));
    }

    @Cacheable("admins")
    @Operation(summary = "buscar admin", description = "admin pode buscar por outro admin especifico")
    @GetMapping("/{adminId}/list")
    public ResponseEntity<AdminResponse> buscarAdmin(@PathVariable Long adminId) {
        log.info("buscar admin");
        return ResponseEntity.ok(adminService.buscarAdmin(adminId, ConstructorErrors.returnMapErrors()));
    }

    @Cacheable("fornecedores")
    @Operation(summary = "listar fornecedores", description = "lista todos os fornecedores")
    @GetMapping("/fornecedores/list")
    public ResponseEntity<Pagina<FornecedorResponse>> listarFornecedores(Pageable pageable) {
        log.info("listar fornecedores");
        return ResponseEntity.ok(adminService.listarFornecedores(pageable));
    }

//    POST /api/v1/categorias/register - Cadastrar nova categoria [ROLE ADMIN]
    @CachePut("categorias")
    @Operation(summary = "cadastrar nova categoria", description = "admin pode cadastrar novas categorias para produtos")
    @PostMapping("/categorias/register")
    public ResponseEntity<CategoriaResponse> cadastrarNovaCategoria(@RequestBody CategoriaRequest categoriaRequest) {
        log.info("cadastrar nova categoria");
        return ResponseEntity.ok(adminService.cadastrarNovaCategoria(categoriaRequest));
    }

//    DELETE /api/v1/clientes/{id} - Remover cliente [ROLE: ADMIN]
    @CacheEvict("clientes")
    @Operation(summary = "remover cliente", description = "admin pode remover a conta de um cliente atraves do id do cliente")
    @DeleteMapping("/users/{userId}/delete")
    public ResponseEntity<String> removerCliente(@PathVariable Long userId) {
        log.info("remover cliente");
        return ResponseEntity.ok(adminService.removerUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

//   GET /api/v1/clientes/{id}/pedidos - Listar pedidos do cliente [ROLE: ADMIN]
    @Cacheable("pedidos")
    @Operation(summary = "listar pedidos de um cliente", description = "admin pode listar os pedidos de um determinado cliente atraves do id do cliente")
    @GetMapping("/clientes/{clienteId}/pedidos/list")
    public ResponseEntity<Pagina<PedidoResponse>> listarPedidosDoCliente(@PathVariable Long clienteId, Pageable pageable) {
        log.info("listar pedidos do cliente");
        return ResponseEntity.ok(adminService.listarPedidosDoCliente(clienteId, pageable, ConstructorErrors.returnMapErrors()));
    }

//    PATCH /api/v1/pedidos/{id}/status/    update - Atualizar status do pedido [ROLE: ADMIN
    @CachePut("pedidos")
    @Operation(summary = "atualizar status do pedido", description = "admin pode atualizar o status de um pedido atraves do id do pedido")
    @PatchMapping("/pedidos/{pedidoId}/status/update")
    public ResponseEntity<PedidoResponse> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoPatchRequest pedidoPatchRequest) {
        log.info("atualizar status de um determinado pedido");
        return ResponseEntity.ok(adminService.atualizarStatusPedido(pedidoId, pedidoPatchRequest, ConstructorErrors.returnMapErrors()));
    }

//    POST /api/v1/users/{userId}/enderecos/register - Cadastrar endereço para Usuarios [ROLE: ADMIN]
    @CachePut("enderecos")
    @Operation(summary = "cadastrar endereco para um usuario", description = "admin pode cadastrar endereco para um usuario especifico")
    @PostMapping("/users/{userId}/enderecos/register")
    public ResponseEntity<EnderecoResponse> cadastrarEnderecoUsuario(@PathVariable Long userId, EnderecoRequest enderecoRequest) {
        log.info("cadastrar endereco de usuario");
        return ResponseEntity.ok(adminService.cadastrarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

//    GET /api/v1/users/{userId}/enderecos/list - Exibir endereco do usuario [ROLE: ADMIN]
    @Cacheable("enderecos")
    @Operation(summary = "exibir endereco do usuario", description = "admin pode exibir o endereco de um usuario especifico")
    @GetMapping("/users/{userId}/endereco/list")
    public ResponseEntity<EnderecoResponse> exibirEnderecoUsuario(@PathVariable Long userId) {
        log.info("exibir endereco de usuario");
        return ResponseEntity.ok(adminService.exibirEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

//    PUT /api/users/{userId}/enderecos/update - Atualizar endereço do usuario [ROLE: ADMIN]
    @CachePut("enderecos")
    @Operation(summary = "atualizar endereco do usuario", description = "admin pode atualizar endereco de um usuario especifico")
    @PutMapping("/users/{userId}/enderecos/update")
    public ResponseEntity<EnderecoResponse> atualizarEnderecoUsuario(@PathVariable Long userId, @RequestBody EnderecoRequest enderecoRequest) {
        log.info("atualizar endereco de usuario");
        return ResponseEntity.ok(adminService.atualizarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

//    DELETE /api/v1/users/{userId}/enderecos/delete` - Remover endereço do usuario [ROLE: ADMIN]
    @CacheEvict("enderecos")
    @Operation(summary = "remover endereco do usuario", description = "admin pode remover o endereco de um usuario especifico")
    @DeleteMapping("/users/{userId}/enderecos/delete")
    public ResponseEntity<String> deletarEnderecoUsuario(@PathVariable Long userId) {
        log.info("deletar endereco de usuario");
        return ResponseEntity.ok(adminService.deletarEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

//    PUT /api/V1/categorias/update - Atualizar categoria [ROLE ADMIN]
    @CachePut("categorias")
    @Operation(summary = "atualizar categoria", description = "admin pode atualizar as informacoes de uma categoria especifica")
    @PutMapping("/categorias/{categoriaId}/update")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaRequest categoriaRequest) {
        log.info("atualizar categoria");
        return ResponseEntity.ok(adminService.atualizarCategoria(categoriaId, categoriaRequest, ConstructorErrors.returnMapErrors()));
    }

//    DELETE /api/categorias/{id}/delete` - Remover categoria [ROLE ADMIN]
    @CacheEvict("categorias")
    @Operation(summary = "deletar categoria", description = "admin pode deletar uma categoria especifica")
    @DeleteMapping("/categorias/{categoriaId}/delete")
    public ResponseEntity<String> deletarCategoria(@PathVariable Long categoriaId) {
        log.info("deletar categoria");
        return ResponseEntity.ok(adminService.deletarCategoria(categoriaId, ConstructorErrors.returnMapErrors()));
    }
}
