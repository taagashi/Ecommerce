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
import br.com.thaua.Ecommerce.exceptions.*;
import br.com.thaua.Ecommerce.mappers.*;
import br.com.thaua.Ecommerce.repositories.*;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
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
        log.info("SERVICE ADMIN - LISTAR ADMINS");
        return paginaMapper.toPagina(pageAdmins);
    }

    public AdminResponse buscarAdmin(Long adminId, Map<String, String> errors) {
        Optional<AdminEntity> adminEntity = adminRepository.findById(adminId);
        validationService.validarExistenciaEntidade(adminEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar admin", UserNotFoundException.class, errors);

        log.info("SERVICE ADMIN - BUSCAR ADMIN");
        return adminMapper.adminEntityToAdminResponse(adminRepository.findById(adminId).get());
    }

    public Pagina<ClienteResponse> listarClientes(Pageable pageable) {
        Page<ClienteResponse> pageClientes = clienteRepository.findAll(pageable).map(clienteMapper::toResponse);

        log.info("SERVICE ADMIN - LISTAR CLIENTES");
        return paginaMapper.toPagina(pageClientes);
    }

    public ClienteResponse buscarCliente(Long clienteId, Map<String, String> errors) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(clienteId);
        validationService.validarExistenciaEntidade(clienteEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar o cliente", UserNotFoundException.class, errors);

        log.info("SERVICE ADMIN - BUCAR CLIENTE");
        return clienteMapper.toResponse(clienteRepository.findById(clienteId).get());
    }

    public FornecedorResponse buscarFornecedor(Long fornecedorId, Map<String, String> errors) {
        Optional<FornecedorEntity> fornecedorEntity = fornecedorRepository.findById(fornecedorId);
        validationService.validarExistenciaEntidade(fornecedorEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar o fornecedor", UserNotFoundException.class, errors);

        log.info("SERVICE ADMIN - BUSCAR FORNECEDOR");
        return fornecedorMapper.FornecedorToResponse(fornecedorEntity.get());
    }

    @Transactional
    public CategoriaResponse cadastrarNovaCategoria(CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaMapper.toEntity(categoriaRequest);

        log.info("SERVICE ADMIN - CADASTRAR NOVA CATEGORIA");
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }

    public Pagina<FornecedorResponse> listarFornecedores(@PageableDefault(size = 2) Pageable pageable) {
        Page<FornecedorResponse> pageFornecedores = fornecedorRepository.findAll(pageable).map(fornecedorMapper::FornecedorToResponse);


        log.info("SERVICE ADMIN - LISTAR FORNECEDORES");
        return paginaMapper.toPagina(pageFornecedores);
    }

    @Transactional
    public String removerUsuario(Long userId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getAdmin().getContasBanidas() == null) {
            usersEntity.getAdmin().setContasBanidas(0);
        }

        Optional<UsersEntity> userDeletar = usersRepository.findById(userId);

        validationService.validarExistenciaEntidade(userDeletar.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro para remover conta de cliente", UserNotFoundException.class, errors);

        usersRepository.delete(userDeletar.get());
        usersEntity.getAdmin().setContasBanidas(usersEntity.getAdmin().getContasBanidas() + 1);
        usersRepository.save(usersEntity);


        log.info("SERVICE ADMIN - REMOVER USUARIO");
        return userDeletar.get().getName() + " foi removido com sucesso";
    }

    public Pagina<PedidoResponse> listarPedidosDoCliente(Long clienteId, Pageable pageable, Map<String, String> errors) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(clienteId);

        validationService.validarExistenciaEntidade(clienteEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro na hora de buscar os pedidos do cliente", PedidoNotFoundException.class, errors);

        log.info("SERVICE ADMIN - LISTAR PEDIDOS DO CLIENTE");
        return paginaMapper.toPagina(pedidoRepository.findAllByClienteId(clienteId, pageable).map(pedidoMapper::toPedidoResponse));
    }

    @Transactional
    public PedidoResponse atualizarStatusPedido(Long pedidoId, PedidoPatchRequest pedidoPatchRequest, Map<String, String> errors) {
        Optional<PedidoEntity> pedidoEntity = pedidoRepository.findById(pedidoId);

        validationService.validarExistenciaEntidade(pedidoEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao atualizar pedido", PedidoNotFoundException.class, errors);

        pedidoEntity.get().setStatusPedido(pedidoPatchRequest.getStatusPedido());


        log.info("SERVICE ADMIN - ATUALIZAR STATUS PEDIDO");
        return pedidoMapper.toPedidoResponse(pedidoRepository.save(pedidoEntity.get()));
    }

    @Transactional
    public EnderecoResponse cadastrarEnderecoUsuario(Long userId, EnderecoRequest enderecoRequest, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaEntidade(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao cadastrar endereco para usuario", AddressNotFoundException.class, errors);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setUsers(usersEntity.get());
        usersEntity.get().setEndereco(enderecoEntity);


        log.info("SERVICE ADMIN - CADASTRAR ENDERECO");
        return enderecoMapper.toEnderecoResponse(usersRepository.save(usersEntity.get()).getEndereco());
    }

    public EnderecoResponse exibirEnderecoUsuario(Long userId, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaEntidade(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao exibir endereco de usuario", AddressNotFoundException.class, errors);


        log.info("SERVICE ADMIN - EXIBIR ENDERECO");
        return enderecoMapper.toEnderecoResponse(usersEntity.get().getEndereco());
    }

    @Transactional
    public EnderecoResponse atualizarEnderecoUsuario(Long userId, EnderecoRequest enderecoRequest, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaEntidade(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar atualizar endereco de usuario", AddressNotFoundException.class, errors);

        EnderecoEntity enderecoEntity = enderecoMapper.enderecoRequestToEntity(enderecoRequest);
        enderecoEntity.setId(usersEntity.get().getEndereco().getId());
        enderecoEntity.setUsers(usersEntity.get());
        usersEntity.get().setEndereco(enderecoEntity);


        log.info("SERVICE ADMIN - ATUALIZAR ENDERECO");
        return enderecoMapper.toEnderecoResponse(enderecoEntity);
    }

    @Transactional
    public String deletarEnderecoUsuario(Long userId, Map<String, String> errors) {
        Optional<UsersEntity> usersEntity = usersRepository.findById(userId);

        validationService.validarExistenciaEntidade(usersEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar deletar endereco de usuario", AddressNotFoundException.class, errors);

        EnderecoEntity enderecoEntity = usersEntity.get().getEndereco();

        usersEntity.get().setEndereco(null);
        enderecoEntity.setUsers(null);

        usersRepository.save(usersEntity.get());
        enderecoRepository.delete(enderecoEntity);


        log.info("SERVICE ADMIN - DELETAR ENDERECO");
        return usersEntity.get().getName() + " teve seu endereco limpo com sucesso";
    }

    @Transactional
    public CategoriaResponse atualizarCategoria(Long categoriaId, CategoriaRequest categoriaRequest, Map<String, String> errors) {
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar atualizar categoria", CategoriaNotFoundException.class, errors);

        categoriaEntity.get().setNome(categoriaRequest.getNome());
        categoriaEntity.get().setDescricao(categoriaRequest.getDescricao());


        log.info("SERVICE ADMIN - ATUALIZAR CATEGORIA");
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity.get()));
    }

    @Transactional
    public String deletarCategoria(Long categoriaId, Map<String, String> errors) {
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar deletar categoria", CategoriaNotFoundException.class, errors);

        categoriaRepository.delete(categoriaEntity.get());


        log.info("SERVICE ADMIN - DELETAR CATEGORIA");
        return "categoria " + categoriaEntity.get().getNome() + " foi deletada com sucesso";
    }


}
