package br.com.thaua.Ecommerce.controllers;

import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.produto.CategoriaComponentResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;

import java.math.BigDecimal;
import java.util.List;

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

    public static CategoriaComponentResponse createCategoriaComponentResponse(Long categoriaId, String nome) {
            CategoriaComponentResponse categoriaComponentResponse = new CategoriaComponentResponse();
            categoriaComponentResponse.setCategoriaId(categoriaId);
            categoriaComponentResponse.setNome(nome);
            return categoriaComponentResponse;
    }

    public static ProdutoCategoriaResponse createProdutoCategoriaResponse(Long id, String nome, String preco, List<CategoriaComponentResponse> categorias) {
        ProdutoCategoriaResponse produtoCategoriaResponse = new ProdutoCategoriaResponse();
        produtoCategoriaResponse.setProdutoId(id);
        produtoCategoriaResponse.setNome(nome);
        produtoCategoriaResponse.setPreco(preco);
        produtoCategoriaResponse.setCategorias(categorias);
        return produtoCategoriaResponse;
    }

    public static ProdutoResponse createProdutoResponse(Long id, String nome, String descricao, BigDecimal preco, Integer estoque, Integer quantidadeDemanda, Integer categoriasAssociadas) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setProdutoId(id);
        produtoResponse.setNome(nome);
        produtoResponse.setDescricao(descricao);
        produtoResponse.setPreco(preco);
        produtoResponse.setEstoque(estoque);
        produtoResponse.setQuantidadeDemanda(quantidadeDemanda);
        produtoResponse.setCategoriasAssociadas(categoriasAssociadas);
        return produtoResponse;
    }

    public static EnderecoRequest createEnderecoRequest(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        EnderecoRequest enderecoRequest = new EnderecoRequest();
        enderecoRequest.setRua(rua);
        enderecoRequest.setBairro(bairro);
        enderecoRequest.setNumero(numero);
        enderecoRequest.setCep(cep);
        enderecoRequest.setCidade(cidade);
        enderecoRequest.setEstado(estado);
        return enderecoRequest;
    }

    public static EnderecoResponse createEnderecoResponse(Long idEndereco, String nameUser, String rua, String numero, String bairro, String cidade, String estado, String cep) {
        EnderecoResponse enderecoResponse = new EnderecoResponse();
        enderecoResponse.setIdEndereco(idEndereco);
        enderecoResponse.setNameUser(nameUser);
        enderecoResponse.setRua(rua);
        enderecoResponse.setNumero(numero);
        enderecoResponse.setBairro(bairro);
        enderecoResponse.setCidade(cidade);
        enderecoResponse.setEstado(estado);
        enderecoResponse.setCep(cep);
        return enderecoResponse;
    }
}
