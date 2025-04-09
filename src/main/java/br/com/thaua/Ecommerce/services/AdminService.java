package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.dto.admin.AdminResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.pedido.PedidoPatchRequest;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.exceptions.UserNotFoundException;
import br.com.thaua.Ecommerce.mappers.*;
import br.com.thaua.Ecommerce.repositories.*;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

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
    private final PaginaMapper paginaMapper;
    private final ValidationService validationService;

    public Pagina<AdminResponse> listarAdmins(Pageable pageable) {
        Page<AdminResponse> pageAdmins = adminRepository.findAll(pageable).map(adminMapper::adminEntityToAdminResponse);
        return paginaMapper.toPagina(pageAdmins);
    }

    public AdminResponse buscarAdmin(Long adminId, Map<String, String> errors) {
        Optional<AdminEntity> adminEntity = adminRepository.findById(adminId);
        validationService.validarExistenciaUsuario(adminEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar admin", UserNotFoundException.class, errors);

        return adminMapper.adminEntityToAdminResponse(adminRepository.findById(adminId).get());
    }
    
    public Pagina<ClienteResponse> listarClientes(Pageable pageable) {
        Page<ClienteResponse> pageClientes = clienteRepository.findAll(pageable).map(clienteMapper::toResponse);
        return paginaMapper.toPagina(pageClientes);
    }


    public ClienteResponse buscarCliente(Long clienteId, Map<String, String> errors) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(clienteId);
        validationService.validarExistenciaUsuario(clienteEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar o cliente", UserNotFoundException.class, errors);

        return clienteMapper.toResponse(clienteRepository.findById(clienteId).get());
    }

    public FornecedorResponse buscarFornecedor(Long fornecedorId, Map<String, String> errors) {
        Optional<FornecedorEntity> fornecedorEntity = fornecedorRepository.findById(fornecedorId);
        validationService.validarExistenciaUsuario(fornecedorEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar o fornecedor", UserNotFoundException.class, errors);

        return fornecedorMapper.FornecedorToResponse(fornecedorEntity.get());
    }

    public CategoriaResponse cadastrarNovaCategoria(CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaMapper.toEntity(categoriaRequest);
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }

    public Pagina<FornecedorResponse> listarFornecedores(@PageableDefault(size = 2) Pageable pageable) {
        Page<FornecedorResponse> pageFornecedores = fornecedorRepository.findAll(pageable).map(fornecedorMapper::FornecedorToResponse);

        return paginaMapper.toPagina(pageFornecedores);
    }

    public String removerCliente(Long clienteId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getAdmin().getContasBanidas() == null) {
            usersEntity.getAdmin().setContasBanidas(0);
        }

        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(clienteId);

        validationService.validarExistenciaUsuario(clienteEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro para remover conta de cliente", UserNotFoundException.class, errors);

        usersRepository.delete(clienteEntity.get().getUsers());
        usersEntity.getAdmin().setContasBanidas(usersEntity.getAdmin().getContasBanidas() + 1);
        usersRepository.save(usersEntity);

        return clienteEntity.get().getUsers().getName() + " foi removido com sucesso";
    }

    public Pagina<PedidoResponse> listarPedidosDoCliente(Long clienteId, Pageable pageable, Map<String, String> errors) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(clienteId);

        validationService.validarExistenciaUsuario(clienteEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar os pedidos do cliente", UserNotFoundException.class, errors);

        return paginaMapper.toPagina(pedidoRepository.findAllByClienteId(clienteId, pageable).map(pedidoMapper::toPedidoResponse));
    }

    public PedidoResponse atualizarStatusPedido(Long pedidoId, PedidoPatchRequest pedidoPatchRequest, Map<String, String> errors) {
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaUsuario(pedidoEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao atualizar pedido", UserNotFoundException.class, errors);

        pedidoEntity.get().setStatusPedido(pedidoPatchRequest.getStatusPedido());

        return pedidoMapper.toPedidoResponse(pedidoRepository.save(pedidoEntity.get()));
    }

    public EnderecoResponse cadastrarEnderecoUsuario(Long userId, EnderecoRequest enderecoRequest, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaUsuario(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao cadastrar endereco para usuario", UserNotFoundException.class, errors);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity.get());
        usersEntity.get().setEndereco(enderecoEntity);
        usersRepository.save(usersEntity.get());

        return enderecoMapper.toEnderecoResponse(enderecoEntity);
    }

    public EnderecoResponse exibirEnderecoUsuario(Long userId, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaUsuario(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao exibir endereco de usuario", UserNotFoundException.class, errors);

        return enderecoMapper.toEnderecoResponse(usersEntity.get().getEndereco());
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
