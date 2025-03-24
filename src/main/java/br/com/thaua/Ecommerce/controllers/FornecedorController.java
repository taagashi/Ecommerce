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
}
