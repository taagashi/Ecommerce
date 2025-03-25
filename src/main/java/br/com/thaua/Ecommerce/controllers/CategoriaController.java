package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categorias/")
public class CategoriaController {
    private final CategoriaService categoriaService;

//    GET /api/v1/categorias/list - Listar todas as categorias [QUALQUER USUARIO AUTENTICADO]
    @Operation(summary = "exibir categorias", description = "usuario autenticado pode ver todas as categorias cadastradas")
    @GetMapping("/list")
    public ResponseEntity<List<CategoriaResponse>> exibirCategorias() {
        return ResponseEntity.ok(categoriaService.exibirCategorias());
    }

//    GET /api/v1/categorias/{categoriaId}/list - Buscar categoria por ID [QUALQUER USUARIO AUTENTICADO]
    @Operation(summary = "exibir categoria", description = "usuario atutenticado pode ver uma categoria especifica procurando pelo id da categoria")
    @GetMapping("/{categoriaId}/list")
    public ResponseEntity<CategoriaResponse> exibirCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(categoriaService.exibirCategoria(categoriaId));
    }
}
