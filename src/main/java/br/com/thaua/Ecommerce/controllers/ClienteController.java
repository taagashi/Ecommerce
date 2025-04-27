package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;
//    PATCH /api/v1/clientes/cpf-telefone/update - atualizar cpf e telfone [ROLE: CLIENTE]
    @Operation(summary = "atualizar cpf e telefone", description = "o cliente autenticado adiciona/atualiza seu numero e cpf para pode realizar acoes mais ativas na aplicacao")
    @PatchMapping("cpf-telefone/update")
    public ResponseEntity<ClienteResponse> atualizarCpfETelefone(@RequestBody ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        log.info("ATUALIZAR CPF E TELEFONE CLIENTE");
        return ResponseEntity.ok(clienteService.atualizarCpfETelefone(clienteCpfTelefoneRequest, ConstructorErrors.returnMapErrors()));
    }

//    PUT /api/v1/clientes/update - Atualizar meus dados [ROLE: CLIENTE]1
    @Operation(summary = "atualizar dados", description = "cliente atualiza dados como: nome, email, telefone e cpf")
    @PutMapping("/update")
    public ResponseEntity<String> atualizarDados(@RequestBody ClienteUpdateRequest clienteUpdateRequest) {
        log.info("ATUALIZAR DADOS CLIENTE");
        return ResponseEntity.ok(clienteService.atualizarDados(clienteUpdateRequest));
    }

//    POST /api/v1/clientes/pedidos/register - Fazer pedido [ROLE: CLIENTE]
    @Operation(summary = "fazer pedido", description = "cliente pode diversos pedidos de uma só vez para diferentes produtos")
    @PostMapping("/pedidos/register")
    public ResponseEntity<PedidoResponse> fazerPedido(@RequestBody List<ItemPedidoRequest> itemPedidoRequest) {
        log.info("FAZER PEDIDO");
        return ResponseEntity.ok(clienteService.fazerPedido(itemPedidoRequest, ConstructorErrors.returnMapErrors()));
    }

//    GET /api/v1/clientes/list/pedidos - Listar meus pedidos [ROLE: CLIENTE]

    @Operation(summary = "listar pedidos", description = "cliente pode listar todos os seus pedidos")
    @GetMapping("/pedidos/list")
    public ResponseEntity<Pagina<PedidoResponse>> listarPedidos(@ParameterObject  Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarPedidos(pageable));
    }

//    GET /api/v1/clientes/pedidos/{id}/list - Buscar pedido por ID [ROLE: CLIENTES]
    @Operation(summary = "listar um pedido", description = "cliente pode listar um pedido especifico atraves do id do pedido")
    @GetMapping("/pedidos/{pedidoId}/list")
    public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(clienteService.buscarPedido(pedidoId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "buscar item pedido", description = "cliente pode buscar um item pedido especifico atraves do id do item pedido")
    @GetMapping("/pedidos/itensPedidos/{itemPedidoId}")
    public ResponseEntity<ItemPedidoResponse> buscarItemPedido(@PathVariable Long itemPedidoId) {
        return ResponseEntity.ok(clienteService.buscarItemPedido(itemPedidoId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "pagar pedido", description = "cliente pode pagar um pedido que foi feito por ele")
    @PostMapping("/pedidos/{pedidoId}/pagar")
    public ResponseEntity<String> pagarPedido(@PathVariable Long pedidoId, @RequestParam("valor") BigDecimal valorPedido) {
        return ResponseEntity.ok(clienteService.pagarPedido(pedidoId, valorPedido, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "editar pedido", description = "cliente pode editar um pedido")
    @PutMapping("/pedidos/{pedidoId}/update")
    public ResponseEntity<PedidoResponse> editarPedido(@PathVariable Long pedidoId, @RequestBody List<ItemPedidoRequest> itemPedidoRequest) {
        log.info("EDITAR PEDIDO");
        return ResponseEntity.ok(clienteService.editarPedido(pedidoId, itemPedidoRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "acrescentar produto a pedido", description = "cliente pode acrescentar mais um item pedido a um pedido que ja foi feito antes")
    @PostMapping("/pedidos/{pedidoId}/update/register")
    public ResponseEntity<PedidoResponse> adicionarProdutoAPedido(@PathVariable Long pedidoId, @RequestParam List<ItemPedidoRequest> itemPedidoRequest) {
        log.info("ADICIONAR PRODUTO A PEDIDO");
        return ResponseEntity.ok(clienteService.adicionarProdutoAPedido(pedidoId, itemPedidoRequest, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "deletar item pedido de um pedido", description = "atraves do id do item pedido, o cliente pode deletar um item pedido")
    @DeleteMapping("/pedidos/itensPedidos/{itemPedidoId}/delete")
    public ResponseEntity<String> deletarItemPedido(@PathVariable Long itemPedidoId) {
        log.info("DELETAR ITEM PEDIDO");
        return ResponseEntity.ok(clienteService.deletarItemPedido(itemPedidoId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "deletar pedido", description = "cliente pode deletar pedido")
    @DeleteMapping("/pedidos/{pedidoId}/delete")
    public ResponseEntity<String> deletarPedido(@PathVariable Long pedidoId) {
        log.info("DELETAR PEDIDO");
        return ResponseEntity.ok(clienteService.deletarPedido(pedidoId, ConstructorErrors.returnMapErrors()));
    }
}
