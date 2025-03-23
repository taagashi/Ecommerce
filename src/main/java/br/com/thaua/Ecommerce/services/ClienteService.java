package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ClienteEntity;
import br.com.thaua.Ecommerce.dto.cliente.ClienteCpfTelefoneRequest;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponse atualizarCpfETelefone(ClienteCpfTelefoneRequest clienteCpfTelefoneRequest) {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClienteEntity clienteEntity = (ClienteEntity) myUserDetails.getTypeUser();
        clienteEntity.setCpf(clienteCpfTelefoneRequest.getCpf());
        clienteEntity.setTelefone(clienteCpfTelefoneRequest.getTelefone());

        return clienteMapper.toResponse(clienteRepository.save(clienteEntity));
    }
}
