package br.com.thaua.Ecommerce;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.domain.enums.Role;
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
import br.com.thaua.Ecommerce.dto.users.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;

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

    public static UsersLoginRequest createUsersLoginRequest(String email, String password) {
        UsersLoginRequest usersLoginRequest = new UsersLoginRequest();
        usersLoginRequest.setEmail(email);
        usersLoginRequest.setPassword(password);
        return usersLoginRequest;
    }

    public static CategoriaEntity createCategoriaEntity(Long id, String nome, String descricao, List<ProdutoEntity> produtos) {
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(id);
        categoriaEntity.setNome(nome);
        categoriaEntity.setDescricao(descricao);
        categoriaEntity.setProdutos(produtos);
        return categoriaEntity;
    }

    public static ProdutoEntity createProdutoEntity(Long id, String nome, String descricao, BigDecimal preco, Integer estoque, List<CategoriaEntity> categorias, List<ItemPedidoEntity> itensPedidos, Integer quantidadeDemanda, FornecedorEntity fornecedor) {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(id);
        produtoEntity.setNome(nome);
        produtoEntity.setDescricao(descricao);
        produtoEntity.setPreco(preco);
        produtoEntity.setEstoque(estoque);
        produtoEntity.setCategorias(categorias);
        produtoEntity.setItensPedidos(itensPedidos);
        produtoEntity.setQuantidadeDemanda(quantidadeDemanda);
        produtoEntity.setFornecedor(fornecedor);
        return produtoEntity;
    }

    public static SimpleMailMessage createSimpleMailMessage(String destinatario, String assunto, String texto, String remetente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(texto);
        message.setFrom(remetente);
        return message;
    }

    public static UsersEntity createUsersEntity(Long userId, String name, String email, String telefone, String password, Role role, AdminEntity admin, ClienteEntity cliente, FornecedorEntity fornecedor, EnderecoEntity endereco) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(userId);
        usersEntity.setName(name);
        usersEntity.setEmail(email);
        usersEntity.setTelefone(telefone);
        usersEntity.setPassword(password);
        usersEntity.setRole(role);
        usersEntity.setAdmin(admin);
        usersEntity.setCliente(cliente);
        usersEntity.setFornecedor(fornecedor);
        usersEntity.setEndereco(endereco);
        return usersEntity;
    }

    public static EnderecoEntity createEnderecoEntity(String rua, String numero, String bairro, String cidade, Estado estado, String cep, UsersEntity usersEntity) {
        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setRua(rua);
        enderecoEntity.setNumero(numero);
        enderecoEntity.setBairro(bairro);
        enderecoEntity.setCidade(cidade);
        enderecoEntity.setEstado(estado);
        enderecoEntity.setCep(cep);
        return enderecoEntity;
    }

    public static CodigoVerificacaoEntity createCodigoVerificacaoEntity(Long id, int codigo) {
        CodigoVerificacaoEntity codigoVerificacaoEntity = new CodigoVerificacaoEntity();
        codigoVerificacaoEntity.setId(id);
        codigoVerificacaoEntity.setCodigo(codigo);
        return codigoVerificacaoEntity;
    }

    public static UsersEntity extractTypeUserContextHolder() {
        Long userId = 4L;
        String name = "joao";
        String email = "joao@gmail.com";
        String password = "senha123";
        Role role = Role.FORNECEDOR;
        String jwtToken = UUID.randomUUID().toString();

        return createUsersEntity(userId, name, email, "+0000000-0000", password, role, null, null, null, null);
    }

    public static ClienteEntity createclienteEntity(Long id, UsersEntity users, String cpf, List<PedidoEntity> pedido) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(id);
        clienteEntity.setUsers(users);
        clienteEntity.setCpf(cpf);
        clienteEntity.setPedido(pedido);
        return clienteEntity;
    }

    public static PedidoEntity createPedidoEntity(Long id, LocalDateTime data, BigDecimal valorPedido, StatusPedido statusPedido, ClienteEntity cliente, List<ItemPedidoEntity> itensPedidos) {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(id);
        pedidoEntity.setDataPedido(data);
        pedidoEntity.setValorPedido(valorPedido);
        pedidoEntity.setStatusPedido(statusPedido);
        pedidoEntity.setCliente(cliente);
        pedidoEntity.setItensPedidos(itensPedidos);
        return pedidoEntity;
    }

    public static ItemPedidoEntity createItemPedidoEntity(Long id, int quantidade, BigDecimal valorTotal, PedidoEntity pedido, ProdutoEntity produto, StatusItemPedido statusItemPedido) {
        ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();
        itemPedidoEntity.setId(id);
        itemPedidoEntity.setQuantidade(quantidade);
        itemPedidoEntity.setValorTotal(valorTotal);
        itemPedidoEntity.setPedido(pedido);
        itemPedidoEntity.setProduto(produto);
        itemPedidoEntity.setStatusItemPedido(statusItemPedido);
        return itemPedidoEntity;
    }
}
