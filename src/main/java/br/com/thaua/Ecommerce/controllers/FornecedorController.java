package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.services.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.thaua.Ecommerce.controllers.handler.ConstructorErrors;
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
        return ResponseEntity.ok(fornecedorService.cadastrarProduto(produtoRequest, ConstructorErrors.returnMapErrors()));
    }

//    GET /api/v1/funcionarios/produtos/list - Listar produtos (com paginação e filtros) [ROLE: FUNCIONARIO]
    @Operation(summary = "listar produtos", description = "Fornecedor faz a listagem de todos os produtos que ele cadastrou")
    @GetMapping("produtos/list")
    public ResponseEntity<Pagina<ProdutoResponse>> exibirProdutos(Pageable pageable) {
        return ResponseEntity.ok(fornecedorService.exibirProdutos(pageable));
    }

//    - `GET /api/v1/funcionarios/produtos/{produtoId}/list - Buscar produto por ID [ROLE: FUNCIONARIO]
    @Operation(summary = "exibir um produto do fornecedor", description = "Fornecedor pode olhar um produto especifico seu")
    @GetMapping("/produtos/{produtoId}")
    public ResponseEntity<ProdutoResponse> buscarProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.buscarProduto(produtoId, ConstructorErrors.returnMapErrors()));
    }

//    PRECISO ARRUMAR ESSA LOGICA AQUI QUE TA LA NO SERVICE
//    PUT /api/v1/funcionarios/produtos/{id}/update - Atualizar produto
    @Operation(summary = "atualizar informacoes de produto", description = "fornecedor pode atualizar as informacoes de um produto especifico")
    @PutMapping("/produtos/{produtoId}/update")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long produtoId, @RequestBody ProdutoRequest produtoRequest) {
        return ResponseEntity.ok(fornecedorService.atualizarProduto(produtoId, produtoRequest, ConstructorErrors.returnMapErrors()));
    }

//    POST /api/v1/funcionarios/categorias/{categoriaId}/produtos/{produtoId}` - Associar produto à categoria [ROLE: FUNCIONARIO]
    @Operation(summary = "adicionar produto para uma categoria", description = "fornecedor pode adicionar um de seus produtos para alguma categoria")
    @PostMapping("/categorias/{categoriaId}/produtos/{produtoId}")
    public ResponseEntity<ProdutoResponse> adicionarProdutoACategoria(@PathVariable Long categoriaId, @PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.adicionarProdutoACategoria(categoriaId, produtoId, ConstructorErrors.returnMapErrors()));
    }

//    PATCH /api/v1/funcionarios/produtos/{id}/estoque/update - Atualizar estoque do produto [ROLE: FUNCIONARIO]
    @Operation(summary = "atualizar estoque", description = "fornecedor pode atualizar o estoque de um produto especifico")
    @PatchMapping("/produtos/{id}/estoque/update")
    public ResponseEntity<ProdutoResponse> atualizarEstoqueProduto(@PathVariable Long id, @RequestBody ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest) {
        return ResponseEntity.ok(fornecedorService.atualizarEstoqueProduto(id, produtoNovoEstoqueRequest, ConstructorErrors.returnMapErrors()));
    }

//    DELETE /api/v1/funcionarios/produtos/{produtoId}/delete - Remover produto [ROLE FUNCIONARIO]
    @Operation(summary = "remover produto", description = "fornecedor pode remover um de seus produtos")
    @DeleteMapping("/produtos/{produtoId}/delete")
    public ResponseEntity<String> removerProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.removerProduto(produtoId, ConstructorErrors.returnMapErrors()));
    }

//  DELETE /api/v1/funcionarios/categorias/{categoriaId}/produtos/{produtoId}/delete` - Remover produto da categoria [ROLE: FUNCIONARIO]
    @Operation(summary = "remover produto de uma categoria", description = "fornecedor pode remover um de seus produtos de uma categoria")
    @DeleteMapping("/categorias/{categoriaId}/produtos/{produtoId}/delete")
    public ResponseEntity<String> removerProdutoDeCategoria(@PathVariable Long categoriaId, @PathVariable Long produtoId) {
        return ResponseEntity.ok(fornecedorService.removerProdutoDeCategoria(categoriaId, produtoId, ConstructorErrors.returnMapErrors()));
    }
}
