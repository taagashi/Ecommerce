package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<CategoriaResponse> exibirCategorias() {
        return categoriaMapper.toResponse(categoriaRepository.findAll());
    }

    public CategoriaResponse exibirCategoria(Long categoriaId) {
        return categoriaMapper.toResponse(categoriaRepository.findById(categoriaId).get());
    }

    public CategoriaProdutosResponse listarProdutosPorCategoria(Long categoriaId) {
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();

        return categoriaMapper.toCategoriaProdutosResponse(categoriaEntity);
    }
}
