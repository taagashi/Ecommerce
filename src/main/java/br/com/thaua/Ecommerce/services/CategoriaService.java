package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaProdutosResponse;
import br.com.thaua.Ecommerce.dto.categoria.CategoriaResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.exceptions.CategoriaNotFoundException;
import br.com.thaua.Ecommerce.mappers.CategoriaMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final PaginaMapper paginaMapper;
    private final ValidationService validationService;

    public Pagina<CategoriaResponse> exibirCategorias(Pageable pageable) {
        return paginaMapper.toPagina(categoriaRepository.findAll(pageable).map(categoriaMapper::toResponse));
    }

    public CategoriaResponse exibirCategoria(Long categoriaId, Map<String, String> errors) {
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar exibir categoria", CategoriaNotFoundException.class, errors);

        return categoriaMapper.toResponse(categoriaEntity.get());
    }

    public CategoriaProdutosResponse listarProdutosPorCategoria(Long categoriaId, Map<String, String> errors) {
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar listar produtos de uma categoria", CategoriaNotFoundException.class, errors);

        return categoriaMapper.toCategoriaProdutosResponse(categoriaEntity.get());
    }
}
