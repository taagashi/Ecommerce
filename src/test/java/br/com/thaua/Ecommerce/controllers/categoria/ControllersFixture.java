package br.com.thaua.Ecommerce.controllers.categoria;

import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.exceptions.AbstractException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ControllersFixture {
    public static CategoriaResponse createCategoriaResponse(Long id, String nome, String descricao, Integer produtosAssociados) {
        CategoriaResponse categoriaResponse = new CategoriaResponse();
        categoriaResponse.setDescricao(descricao);
        categoriaResponse.setId(id);
        categoriaResponse.setNome(nome);
        categoriaResponse.setProdutosAssociados(produtosAssociados);
        return categoriaResponse;
    }

    public static String createErrorMessage(String message) {
        return message;
    }

    public static ProdutoComponentResponse createProdutoComponentResponse(Long id, String nome, BigDecimal preco, Integer estoque) {
        ProdutoComponentResponse produtoComponentResponse = new ProdutoComponentResponse();
        produtoComponentResponse.setProdutoId(id);
        produtoComponentResponse.setNome(nome);
        produtoComponentResponse.setPreco(preco);
        produtoComponentResponse.setEstoque(estoque);
        return produtoComponentResponse;
    }

    public static CategoriaProdutosResponse createCategoriaProdutosResponse(Long id, String nome, String descricao, List<ProdutoComponentResponse> produtos) {
        CategoriaProdutosResponse categoriaProdutosResponse = new CategoriaProdutosResponse();
        categoriaProdutosResponse.setCategoriaId(id);
        categoriaProdutosResponse.setNome(nome);
        categoriaProdutosResponse.setDescricao(descricao);
        categoriaProdutosResponse.setProdutos(produtos);
        return categoriaProdutosResponse;
    }
}
