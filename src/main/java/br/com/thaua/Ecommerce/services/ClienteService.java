package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.cliente.ClienteComPedidoResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteUpdateRequest;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final UsersRepository usersRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponse atualizarCpfETelefone(ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.getCliente().setCpf(clienteCpfTelefoneRequest.getCpf());
        usersEntity.getCliente().setTelefone(clienteCpfTelefoneRequest.getTelefone());

        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }

    public ClienteComPedidoResponse exibirPerfil() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        return clienteMapper.toResponseComPedido(usersEntity.getCliente());
    }


    public ClienteResponse atualizarDados(ClienteUpdateRequest clienteUpdateRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.setName(clienteUpdateRequest.getName());
        usersEntity.setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setName(clienteUpdateRequest.getName());
        usersEntity.getCliente().setEmail(clienteUpdateRequest.getEmail());
        usersEntity.getCliente().setCpf(clienteUpdateRequest.getCpf());
        usersEntity.getCliente().setTelefone(clienteUpdateRequest.getTelefone());

        return clienteMapper.toResponse(usersRepository.save(usersEntity).getCliente());
    }
}
