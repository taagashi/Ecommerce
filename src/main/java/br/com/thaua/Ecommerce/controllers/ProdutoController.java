package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    @Operation(summary = "Lista todas as categorias de um determinado produto", description = "faz a listagem de categorias de um produto atraves de seu id")
    @GetMapping("/{produtoId}/categorias/list")
    public ResponseEntity<ProdutoCategoriaResponse> exibirCategoriasDeProduto(@PathVariable Long produtoId) {
        log.info("CONTROLLER PRODUTO - EXIBIR CATEGORIAS DE PRODUTO");
        return ResponseEntity.ok(produtoService.exibirCategoriasDeProduto(produtoId, ConstructorErrors.returnMapErrors()));
    }

    @Operation(summary = "Lista todos os produtos com filtrando os precos", description = "lista todos os produtos usando um filtro de precos para o usuario poder ver qual eh o produto mais barato e afins")
    @GetMapping("/list")
    public ResponseEntity<Pagina<ProdutoResponse>> exibirProdutos(
            @ParameterObject
            Pageable pageable,

            @Parameter(description = "preco minimo")
            @RequestParam(required = false) BigDecimal min,

            @Parameter(description = "preco maximo")
            @RequestParam(required = false) BigDecimal max
    ) {
        log.info("CONTROLLER PRODUTO - EXIBIR PRODUTOS");
        return ResponseEntity.ok(produtoService.exibirProdutos(pageable, min, max));
    }

    @Operation(summary = "Buscar produto por id", description = "usuario cadastrado pode buscar um produto pelo id")
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponse> buscarProduto(@PathVariable Long produtoId) {
        log.info("CONTROLLER PRODUTO - BUSCAR PRODUTO");
        return ResponseEntity.ok(produtoService.buscarProduto(produtoId, ConstructorErrors.returnMapErrors()));
    }
}
