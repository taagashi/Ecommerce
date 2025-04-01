package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pedido.PedidoResponse;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.mappers.PedidoMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import br.com.thaua.Ecommerce.repositories.FornecedorRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import lombok.RequiredArgsConstructor;
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

    public List<ClienteResponse> listarClientes() {
        return clienteMapper.toResponse(clienteRepository.findAll());
    }

    public CategoriaResponse cadastrarNovaCategoria(CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaMapper.toEntity(categoriaRequest);
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }

    public List<FornecedorResponse> listarFornecedores() {
        return fornecedorMapper.toFornecedorResponseListResponse(fornecedorRepository.findAll());
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

        return clienteEntity.getName() + " foi removido com sucesso";
    }

    public List<PedidoResponse> listarPedidosDoCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId).get();

        return pedidoMapper.toPedidoResponseList(clienteEntity.getPedido());
    }
}
