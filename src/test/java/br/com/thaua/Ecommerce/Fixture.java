package br.com.thaua.Ecommerce;

import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.categoria.ProdutoComponentResponse;
import br.com.thaua.Ecommerce.dto.cliente.*;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.*;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.dto.produto.*;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateCode;
import br.com.thaua.Ecommerce.dto.users.UserRequestGenerateNewPassword;
import br.com.thaua.Ecommerce.dto.users.UsersRequest;
import br.com.thaua.Ecommerce.dto.users.UsersResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;

public class Fixture {
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

    public static UsersRequest createUserRequest(String nome, String email, String password, String role) {
        UsersRequest usersRequest = new UsersRequest();
        usersRequest.setName(nome);
        usersRequest.setEmail(email);
        usersRequest.setPassword(password);
        usersRequest.setRole(role);
        return usersRequest;
    }

    public static UsersResponse createUserResponse(String nome, String email) {
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setName(nome);
        usersResponse.setEmail(email);
        return usersResponse;
    }

    public static String createJwtToken() {
        return UUID.randomUUID().toString();
    }

    public static UserRequestGenerateCode createUserRequestGenerateCode(String email) {
        UserRequestGenerateCode userRequestGenerateCode = new UserRequestGenerateCode();
        userRequestGenerateCode.setEmail(email);
        return userRequestGenerateCode;
    }

    public static int createCodigoVerificacao() {
        return 123456;
    }

    public static UserRequestGenerateNewPassword createUserRequestGenerateNewPassword(String email, String newPassword, int code) {
        UserRequestGenerateNewPassword userRequestGenerateNewPassword = new UserRequestGenerateNewPassword();
        userRequestGenerateNewPassword.setEmail(email);
        userRequestGenerateNewPassword.setNewPassword(newPassword);
        userRequestGenerateNewPassword.setCode(code);
        return userRequestGenerateNewPassword;
    }

    public static AdminResponse createAdminResponse(Long id, String name, String email, Integer contasBanidas, LocalDateTime ultimoAcesso) {
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setId(id);
        adminResponse.setName(name);
        adminResponse.setEmail(email);
        adminResponse.setContasBanidas(contasBanidas);
        adminResponse.setUltimoAcesso(ultimoAcesso);
        return adminResponse;
    }

    public static ClienteCpfTelefoneRequest createClienteCpfTelefoneRequest(String cpf, String telefone) {
        ClienteCpfTelefoneRequest clienteCpfTelefoneRequest = new ClienteCpfTelefoneRequest();
        clienteCpfTelefoneRequest.setCpf(cpf);
        clienteCpfTelefoneRequest.setTelefone(telefone);
        return clienteCpfTelefoneRequest;
    }

    public static ClienteResponse createClienteResponse(Long id, String name, String email, String telefone, String cpf) {
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setId(id);
        clienteResponse.setName(name);
        clienteResponse.setEmail(email);
        clienteResponse.setTelefone(telefone);
        clienteResponse.setCpf(cpf);
        return clienteResponse;
    }

    public static ClienteUpdateRequest createClienteUpdateRequest(String name, String email, String telefone, String cpf) {
        ClienteUpdateRequest clienteUpdateRequest = new ClienteUpdateRequest();
        clienteUpdateRequest.setName(name);
        clienteUpdateRequest.setEmail(email);
        clienteUpdateRequest.setTelefone(telefone);
        clienteUpdateRequest.setCpf(cpf);
        return clienteUpdateRequest;
    }

    public static ItemPedidoRequest createItemPedidoRequest(Long produtoId, int quantidade) {
        ItemPedidoRequest itemPedidoRequest = new ItemPedidoRequest();
        itemPedidoRequest.setProdutoId(produtoId);
        itemPedidoRequest.setQuantidade(quantidade);
        return itemPedidoRequest;
    }

    public static ItemPedidoResponse createItemPedidoResponse(Long itemPedidoId, Long produtoId, String produto, int quantidade, BigDecimal valorTotal, StatusItemPedido statusItemPedido) {
        ItemPedidoResponse itemPedidoResponse = new ItemPedidoResponse();
        itemPedidoResponse.setItemPedidoId(itemPedidoId);
        itemPedidoResponse.setProdutoId(produtoId);
        itemPedidoResponse.setProduto(produto);
        itemPedidoResponse.setQuantidade(quantidade);
        itemPedidoResponse.setValorTotal(valorTotal);
        itemPedidoResponse.setStatusItemPedido(statusItemPedido);
        return itemPedidoResponse;
    }

    public static PedidoResponse createPedidoResponse(Long pedidoId, String cliente, LocalDateTime dataPedido, BigDecimal valorPedido, String statusPedido, List<ItemPedidoResponse> itensPedidos) {
        PedidoResponse pedidoResponse = new PedidoResponse();
        pedidoResponse.setPedidoId(pedidoId);
        pedidoResponse.setCliente(cliente);
        pedidoResponse.setDataPedido(dataPedido);
        pedidoResponse.setValorPedido(valorPedido);
        pedidoResponse.setStatusPedido(statusPedido);
        pedidoResponse.setItensPedidos(itensPedidos);
        return pedidoResponse;
    }

    public static FornecedorCNPJTelefoneRequest createFornecedorCNPJTelefoneRequest(String cnpj, String telefone) {
        FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest = new FornecedorCNPJTelefoneRequest();
        fornecedorCNPJTelefoneRequest.setCnpj(cnpj);
        fornecedorCNPJTelefoneRequest.setTelefone(telefone);
        return fornecedorCNPJTelefoneRequest;
    }

    public static FornecedorResponse createFornecedorResponse(Long id, String name, String email, String telefone, String cnpj) {
        FornecedorResponse fornecedorResponse = new FornecedorResponse();
        fornecedorResponse.setId(id);
        fornecedorResponse.setName(name);
        fornecedorResponse.setEmail(email);
        fornecedorResponse.setTelefone(telefone);
        fornecedorResponse.setCnpj(cnpj);
        return fornecedorResponse;
    }

    public static ProdutoRequest createProdutoRequest(String nome, String descricao, BigDecimal preco, int estoque) {
        ProdutoRequest produtoRequest = new ProdutoRequest();
        produtoRequest.setNome(nome);
        produtoRequest.setDescricao(descricao);
        produtoRequest.setPreco(preco);
        produtoRequest.setEstoque(estoque);
        return produtoRequest;
    }

    public static ProdutoNovoEstoqueRequest createProdutoNovoEstoqueRequest(int novaQuantidade) {
        ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest = new ProdutoNovoEstoqueRequest();
        produtoNovoEstoqueRequest.setNovaQuantidade(novaQuantidade);
        return produtoNovoEstoqueRequest;
    }

    public static FornecedorSaldoResponse createFornecedorSaldoResponse(BigDecimal saldo) {
        FornecedorSaldoResponse fornecedorSaldoResponse = new FornecedorSaldoResponse();
        fornecedorSaldoResponse.setSaldo(saldo);
        return fornecedorSaldoResponse;
    }

    public static CategoriaRequest createCategoriaRequest(String nome, String descricao) {
        CategoriaRequest categoriaRequest = new CategoriaRequest();
        categoriaRequest.setNome(nome);
        categoriaRequest.setDescricao(descricao);
        return categoriaRequest;
    }

    public static PedidoPatchRequest createPedidoPatchRequest(StatusPedido statusPedido) {
        PedidoPatchRequest pedidoPatchRequest = new PedidoPatchRequest();
        pedidoPatchRequest.setStatusPedido(statusPedido);
        return pedidoPatchRequest;
    }

    public static ClienteComPedidoResponse createClienteComPedidoResponse(Long id, String name, String email, String telefone, String cpf, int pedidosFeitos) {
        ClienteComPedidoResponse clienteComPedidoResponse = new ClienteComPedidoResponse();
        clienteComPedidoResponse.setId(id);
        clienteComPedidoResponse.setName(name);
        clienteComPedidoResponse.setEmail(email);
        clienteComPedidoResponse.setTelefone(telefone);
        clienteComPedidoResponse.setCpf(cpf);
        clienteComPedidoResponse.setPedidosFeitos(pedidosFeitos);
        return clienteComPedidoResponse;
    }

    public static FornecedorViewProfileResponse createFornecedorViewProfileResponse(Long id, String name, String email, String telefone, String cnpj, BigDecimal saldo, Integer produtosEnviados, Integer produtosCadastrados) {
        FornecedorViewProfileResponse fornecedorViewProfileResponse = new FornecedorViewProfileResponse();
        fornecedorViewProfileResponse.setId(id);
        fornecedorViewProfileResponse.setName(name);
        fornecedorViewProfileResponse.setEmail(email);
        fornecedorViewProfileResponse.setTelefone(telefone);
        fornecedorViewProfileResponse.setCnpj(cnpj);
        fornecedorViewProfileResponse.setSaldo(saldo);
        fornecedorViewProfileResponse.setProdutosEnviados(produtosEnviados);
        fornecedorViewProfileResponse.setProdutosCadastrados(produtosCadastrados);
        return fornecedorViewProfileResponse;
    }
}
