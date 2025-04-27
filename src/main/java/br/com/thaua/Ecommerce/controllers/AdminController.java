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
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(summary = "Listar clientes", description = "Lista todos os clientes")
    @GetMapping("/clientes")
    public ResponseEntity<Pagina<ClienteResponse>> listarClientes(@ParameterObject  Pageable pageable) {
        log.info("CONTROLLER ADMIN - LISTAR CLIENTES");
        return ResponseEntity.ok(adminService.listarClientes(pageable));
    }

    @Operation(summary = "buscar cliente", description = "admin pode buscar um cliente especifico")
    @GetMapping("/clientes/{clienteId}/list")
    public ResponseEntity<ClienteResponse> buscarCliente(@PathVariable Long clienteId) {
        log.info("CONTROLLER ADMIN - BUSCAR CLIENTE");
        return ResponseEntity.ok(adminService.buscarCliente(clienteId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "buscar fornecedor", description = "admin pode buscar um fornecedor especifico")
    @GetMapping("/fornecedores/{fornecedorId}/list")
    public ResponseEntity<FornecedorResponse> buscarFornecedor(@PathVariable Long fornecedorId) {
        log.info("CONTROLLER ADMIN - BUSCAR FORNECEDOR");
        return ResponseEntity.ok(adminService.buscarFornecedor(fornecedorId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "listar fornecedores", description = "lista todos os fornecedores")
    @GetMapping("/fornecedores/list")
    public ResponseEntity<Pagina<FornecedorResponse>> listarFornecedores(@ParameterObject  Pageable pageable) {
        log.info("CONTROLLER ADMIN - LISTAR FORNECEDORES");
        return ResponseEntity.ok(adminService.listarFornecedores(pageable));
    }

    @Operation(summary = "listar admin's", description = "admin pode ver todos os admin's registrados")
    @GetMapping("/list")
    public ResponseEntity<Pagina<AdminResponse>> listarAdmins(@ParameterObject Pageable pageable) {
        log.info("CONTROLLER ADMIN - LISTAR ADMINS");
        return ResponseEntity.ok(adminService.listarAdmins(pageable));
    }

    @Operation(summary = "buscar admin", description = "admin pode buscar por outro admin especifico")
    @GetMapping("/{adminId}/list")
    public ResponseEntity<AdminResponse> buscarAdmin(@PathVariable Long adminId) {
        log.info("CONTROLLER ADMIN - BUSCAR ADMIN");
        return ResponseEntity.ok(adminService.buscarAdmin(adminId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "cadastrar nova categoria", description = "admin pode cadastrar novas categorias para produtos")
    @PostMapping("/categorias/register")
    public ResponseEntity<CategoriaResponse> cadastrarNovaCategoria(@RequestBody CategoriaRequest categoriaRequest) {
        log.info("CONTROLLER ADMIN - CADASTRAR NOVA CATEGORIA");
        return ResponseEntity.ok(adminService.cadastrarNovaCategoria(categoriaRequest));
    }

    @Operation(summary = "remover usuario", description = "admin pode remover a conta de um determinado usuario")
    @DeleteMapping("/users/{userId}/delete")
    public ResponseEntity<String> removerUsuario(@PathVariable Long userId) {
        log.info("CONTROLLER ADMIN - REMOVER CLIENTE");
        return ResponseEntity.ok(adminService.removerUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "listar pedidos de um cliente", description = "admin pode listar os pedidos de um determinado cliente atraves do id do cliente")
    @GetMapping("/clientes/{clienteId}/pedidos/list")
    public ResponseEntity<Pagina<PedidoResponse>> listarPedidosDoCliente(@PathVariable Long clienteId, @ParameterObject Pageable pageable) {
        log.info("CONTROLLER ADMIN - LISTAR PEDIDOS CLIENTE");
        return ResponseEntity.ok(adminService.listarPedidosDoCliente(clienteId, pageable, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "atualizar status do pedido", description = "admin pode atualizar o status de um pedido atraves do id do pedido")
    @PatchMapping("/pedidos/{pedidoId}/status/update")
    public ResponseEntity<PedidoResponse> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody PedidoPatchRequest pedidoPatchRequest) {
        log.info("CONTROLLER ADMIN - ATUALIZAR STATUS PEDIDO");
        return ResponseEntity.ok(adminService.atualizarStatusPedido(pedidoId, pedidoPatchRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "cadastrar endereco para um usuario", description = "admin pode cadastrar endereco para um usuario especifico")
    @PostMapping("/users/{userId}/enderecos/register")
    public ResponseEntity<EnderecoResponse> cadastrarEnderecoUsuario(@PathVariable Long userId, EnderecoRequest enderecoRequest) {
        log.info("CONTROLLER ADMIN - CADASTRAR ENDERECO USUARIO");
        return ResponseEntity.ok(adminService.cadastrarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "exibir endereco do usuario", description = "admin pode exibir o endereco de um usuario especifico")
    @GetMapping("/users/{userId}/endereco/list")
    public ResponseEntity<EnderecoResponse> exibirEnderecoUsuario(@PathVariable Long userId) {
        log.info("CONTROLLER ADMIN - EXIBIR ENDERECO USUARIO");
        return ResponseEntity.ok(adminService.exibirEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "atualizar endereco do usuario", description = "admin pode atualizar endereco de um usuario especifico")
    @PutMapping("/users/{userId}/enderecos/update")
    public ResponseEntity<EnderecoResponse> atualizarEnderecoUsuario(@PathVariable Long userId, @RequestBody EnderecoRequest enderecoRequest) {
        log.info("CONTROLLER ADMIN - ATUALIZAR ENDERECO USUARIO");
        return ResponseEntity.ok(adminService.atualizarEnderecoUsuario(userId, enderecoRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "remover endereco do usuario", description = "admin pode remover o endereco de um usuario especifico")
    @DeleteMapping("/users/{userId}/enderecos/delete")
    public ResponseEntity<String> deletarEnderecoUsuario(@PathVariable Long userId) {
        log.info("CONTROLLER ADMIN - DELETAR ENDERECO USUARIO");
        return ResponseEntity.ok(adminService.deletarEnderecoUsuario(userId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "atualizar categoria", description = "admin pode atualizar as informacoes de uma categoria especifica")
    @PutMapping("/categorias/{categoriaId}/update")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaRequest categoriaRequest) {
        log.info("CONTROLLER ADMIN - ATUALIZAR CATEGORIA");
        return ResponseEntity.ok(adminService.atualizarCategoria(categoriaId, categoriaRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "deletar categoria", description = "admin pode deletar uma categoria especifica")
    @DeleteMapping("/categorias/{categoriaId}/delete")
    public ResponseEntity<String> deletarCategoria(@PathVariable Long categoriaId) {
        log.info("CONTROLLER ADMIN - DELETAR CATEGORIA");
        return ResponseEntity.ok(adminService.deletarCategoria(categoriaId, ConstructorErrors.returnMapErrors()));
    }
}
