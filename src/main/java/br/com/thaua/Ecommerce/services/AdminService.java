package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.mappers.*;
import br.com.thaua.Ecommerce.repositories.*;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ClienteRepository clienteRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ClienteMapper clienteMapper;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final FornecedorMapper fornecedorMapper;
    private final UsersRepository usersRepository;
    private final PedidoMapper pedidoMapper;
    private final PedidoRepository pedidoRepository;
    private final EnderecoMapper enderecoMapper;
    private final EnderecoRepository enderecoRepository;
    private final AdminMapper adminMapper;
    private final AdminRepository adminRepository;

    public List<AdminResponse> exibirAdmins() {
        return adminMapper.adminEntityToAdminResponseList(adminRepository.findAll());
    }

    public AdminResponse buscarAdmin(Long adminId) {
        return adminMapper.adminEntityToAdminResponse(adminRepository.findById(adminId).get());
    }
    
    public List<ClienteResponse> listarClientes() {
        return clienteMapper.toResponse(clienteRepository.findAll());
    }


    public ClienteResponse buscarCliente(Long clienteId) {
        return clienteMapper.toResponse(clienteRepository.findById(clienteId).get());
    }

    public FornecedorResponse buscarFornecedor(Long fornecedorId) {
        return fornecedorMapper.FornecedorToResponse(fornecedorRepository.findById(fornecedorId).get());
    }
    public CategoriaResponse cadastrarNovaCategoria(CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaMapper.toEntity(categoriaRequest);
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }

    public Page<FornecedorResponse> listarFornecedores(@PageableDefault(size = 2) Pageable pageable) {
        return fornecedorRepository.findAll(pageable).map(fornecedorMapper::FornecedorToResponse);
    }

    public String removerCliente(Long clienteId) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getAdmin().getContasBanidas() == null) {
            usersEntity.getAdmin().setContasBanidas(0);
        }

        ClienteEntity clienteEntity = clienteRepository.findById(clienteId).get();
        usersRepository.delete(clienteEntity.getUsers());
        usersEntity.getAdmin().setContasBanidas(usersEntity.getAdmin().getContasBanidas() + 1);
        usersRepository.save(usersEntity);

        return clienteEntity.getUsers().getName() + " foi removido com sucesso";
    }

    public List<PedidoResponse> listarPedidosDoCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId).get();

        return pedidoMapper.toPedidoResponseList(clienteEntity.getPedido());
    }

    public PedidoResponse atualizarStatusPedido(Long pedidoId, PedidoPatchRequest pedidoPatchRequest) {
        PedidoEntity pedidoEntity = pedidoRepository.findById(pedidoId).get();
        pedidoEntity.setStatusPedido(pedidoPatchRequest.getStatusPedido());

        return pedidoMapper.toPedidoResponse(pedidoRepository.save(pedidoEntity));
    }

    public EnderecoResponse cadastrarEnderecoUsuario(Long userId, EnderecoRequest enderecoRequest) {
        UsersEntity usersEntity = usersRepository.findById(userId).get();
        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity);
        usersEntity.setEndereco(enderecoEntity);
        usersRepository.save(usersEntity);

        return enderecoMapper.toEnderecoResponse(enderecoEntity);
    }

    public EnderecoResponse exibirEnderecoUsuario(Long userId) {
        UsersEntity usersEntity = usersRepository.findById(userId).get();

        return enderecoMapper.toEnderecoResponse(usersEntity.getEndereco());
    }

    public EnderecoResponse atualizarEnderecoUsuario(Long userId, EnderecoRequest enderecoRequest) {
        UsersEntity usersEntity = usersRepository.findById(userId).get();
        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setId(usersEntity.getEndereco().getId());
        enderecoEntity.setUsers(usersEntity);
        usersEntity.setEndereco(enderecoEntity);

        return enderecoMapper.toEnderecoResponse(enderecoEntity);
    }

    public String deletarEnderecoUsuario(Long userId) {
        UsersEntity usersEntity = usersRepository.findById(userId).get();
        EnderecoEntity enderecoEntity = usersEntity.getEndereco();

        usersEntity.setEndereco(null);
        enderecoEntity.setUsers(null);

        usersRepository.save(usersEntity);
        enderecoRepository.delete(enderecoEntity);

        return usersEntity.getName() + " teve seu endereco limpo com sucesso";
    }

    public CategoriaResponse atualizarCategoria(Long categoriaId, CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();

        categoriaEntity.setNome(categoriaRequest.getNome());
        categoriaEntity.setDescricao(categoriaRequest.getDescricao());

        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }

    public String deletarCategoria(Long categoriaId) {
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();

        categoriaRepository.delete(categoriaEntity);

        return "categoria " + categoriaEntity.getNome() + " foi deletada com sucesso";
    }


}
