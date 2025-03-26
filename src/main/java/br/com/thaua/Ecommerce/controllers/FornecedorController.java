package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.services.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/fornecedores")
public class FornecedorController {
    private final FornecedorService fornecedorService;

    //    PATCH /api/v1/funcionarios/cnpj-telefone/update - atualizar cnpj e telfone [ROLE: FUNCIONARIOS]
    @Operation(summary = "atualizar CNPJ e telefone", description = "fornecedor pode atualizar dados como cnpj e telefone")
    @PatchMapping("/cnpj-telefone/update")
    public ResponseEntity<FornecedorResponse> atualizarCNPJeTelefone(@RequestBody FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest) {
        return ResponseEntity.ok(fornecedorService.atualizarCNPJeTelefone(fornecedorCNPJTelefoneRequest));
    }

//    POST /api/V1/funcionarios/produtos/register - Cadastrar novo produto [ROLE FUNCIONARIO]
    @Operation(summary = "cadastrar produto", description = "fornecedor só pode cadastrar produtos se tiver com CNPJ, telefone e endereço cadastrados")
    @PostMapping("/produtos/register")
    public ResponseEntity<ProdutoResponse> cadastrarProduto(@RequestBody ProdutoRequest produtoRequest) {
        return ResponseEntity.ok(fornecedorService.cadastrarProduto(produtoRequest));
    }

//    GET /api/v1/funcionarios/produtos/list - Listar produtos (com paginação e filtros) [ROLE: FUNCIONARIO]
    @Operation(summary = "listar produtos", description = "Fornecedor faz a listagem de todos os produtos que ele cadastrou")
    @GetMapping("produtos/list")
    public ResponseEntity<List<ProdutoResponse>> exibirProdutos() {
        return ResponseEntity.ok(fornecedorService.exibirProdutos());
    }

//    - `GET /api/v1/funcionarios/produtos/{produtoId}/list - Buscar produto por ID [ROLE: FUNCIONARIO]
    @Operation(summary = "exibir um produto do fornecedor", description = "Fornecedor pode olhar um produto especifico seu")
    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<ProdutoResponse> buscarProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.buscarProduto(produtoId));
    }

//    PUT /api/v1/funcionarios/produtos/{id}/update - Atualizar produto
    @Operation(summary = "atualizar informacoes de produto", description = "fornecedor pode atualizar as informacoes de um produto especifico")
    @PutMapping("/produtos/{produtoId}/update")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long produtoId, @RequestBody ProdutoRequest produtoRequest) {
        return ResponseEntity.ok(fornecedorService.atualizarProduto(produtoId, produtoRequest));
    }

//    POST /api/v1/funcionarios/categorias/{categoriaId}/produtos/{produtoId}` - Associar produto à categoria [ROLE: FUNCIONARIO]
    @Operation(summary = "adicionar produto para uma categoria", description = "fornecedor pode adicionar um de seus produtos para alguma categoria")
    @PostMapping("/categorias/{categoriaId}/produtos/{produtoId}")
    public ResponseEntity<String> adicionarProdutoACategoria(@PathVariable Long categoriaId, @PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.adicionarProdutoACategoria(categoriaId, produtoId));
    }

}
