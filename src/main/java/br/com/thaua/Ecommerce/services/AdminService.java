package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.dto.ClienteResponse;
import br.com.thaua.Ecommerce.mappers.Converter;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ClienteRepository clienteRepository;
    private final Converter converter;

    public List<ClienteResponse> listarClientes() {
        return converter.toResponse(clienteRepository.findAll());
    }
}
