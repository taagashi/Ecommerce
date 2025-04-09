package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final PaginaMapper paginaMapper;

    public Pagina<CategoriaResponse> exibirCategorias(Pageable pageable) {
        return paginaMapper.toPagina(categoriaRepository.findAll(pageable).map(categoriaMapper::toResponse));
    }

    public CategoriaResponse exibirCategoria(Long categoriaId) {
        return categoriaMapper.toResponse(categoriaRepository.findById(categoriaId).get());
    }

    public CategoriaProdutosResponse listarProdutosPorCategoria(Long categoriaId) {
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();

        return categoriaMapper.toCategoriaProdutosResponse(categoriaEntity);
    }
}
