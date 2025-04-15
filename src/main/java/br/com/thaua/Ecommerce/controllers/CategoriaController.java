package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categorias/")
public class CategoriaController {
    private final CategoriaService categoriaService;

//    GET /api/v1/categorias/list - Listar todas as categorias [QUALQUER USUARIO AUTENTICADO]
    @Operation(summary = "exibir categorias", description = "usuario autenticado pode ver todas as categorias cadastradas")
    @GetMapping("/list")
    public ResponseEntity<Pagina<CategoriaResponse>> exibirCategorias(Pageable pageable) {
        log.info("EXIBINDO CATEGORIAS");
        return ResponseEntity.ok(categoriaService.exibirCategorias(pageable));
    }

//    GET /api/v1/categorias/{categoriaId}/list - Buscar categoria por ID [QUALQUER USUARIO AUTENTICADO]
    @Operation(summary = "exibir categoria", description = "usuario atutenticado pode ver uma categoria especifica procurando pelo id da categoria")
    @GetMapping("/{categoriaId}/list")
    public ResponseEntity<CategoriaResponse> exibirCategoria(@PathVariable Long categoriaId) {
        log.info("EXIBINDO UMA CATEGORIA");
        return ResponseEntity.ok(categoriaService.exibirCategoria(categoriaId, ConstructorErrors.returnMapErrors()));
    }

//    GET /api/v1/categorias/{categoriaId}/produtos/list` - Listar produtos por categoria [QUALQUER USUARIO AUTENTICADO]
    @Operation(summary = "exibir produtos de uma categoria", description = "usuario autenticado pode ver todos os produtos de uma categoria")
    @GetMapping("/{categoriaId}/produtos/list")
    public ResponseEntity<CategoriaProdutosResponse> listarProdutosPorCategoria(@PathVariable Long categoriaId) {
        log.info("LISTANDO PRODUTOS POR CATEGORIA");
        return ResponseEntity.ok(categoriaService.listarProdutosPorCategoria(categoriaId, ConstructorErrors.returnMapErrors()));
    }
}
