package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    @Operation(summary = "Lista todas as categorias de um determinado produto", description = "faz a listagem de categorias de um produto atraves de seu id")
    @GetMapping("/{produtoId}/categorias/list")
    public ResponseEntity<ProdutoCategoriaResponse> exibirCategoriasDeProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(produtoService.exibirCategoriasDeProduto(produtoId));
    }
}
