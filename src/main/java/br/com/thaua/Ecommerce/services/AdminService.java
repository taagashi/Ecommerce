package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaRequest;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.cliente.ClienteResponse;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.mappers.ClienteMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<ClienteResponse> listarClientes() {
        return clienteMapper.toResponse(clienteRepository.findAll());
    }

    public CategoriaResponse cadastrarNovaCategoria(CategoriaRequest categoriaRequest) {
        CategoriaEntity categoriaEntity = categoriaMapper.toEntity(categoriaRequest);
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaEntity));
    }
}
