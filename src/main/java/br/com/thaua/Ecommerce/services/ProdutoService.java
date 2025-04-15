package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.ProdutoNotFoundException;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.specifications.ProdutoSpecifications;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ProdutoSpecifications produtoSpecifications;
    private final PaginaMapper paginaMapper;
    private final ValidationService validationService;

    @Cacheable("categoria-Produto")
    public ProdutoCategoriaResponse exibirCategoriasDeProduto(Long produtoId, Map<String, String> errors) {
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar exibir categorias de um produto", ProdutoNotFoundException.class, errors);


        log.info("EXECUTANDO SERVICE-PRODUTO EXIBIR CATEGORIAS PRODUTOS");
        return produtoMapper.toProdutoCategoriaResponse(produtoEntity.get());
    }

    @Cacheable("produtos")
    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable, BigDecimal min, BigDecimal max) {

        log.info("EXECUTANDO SERVICE-PRODUTO EXIBIR PRODUTOS");
        return paginaMapper.toPagina(produtoSpecifications.buscarComFiltros(min, max, pageable).map(produtoMapper::produtoToResponse));
    }

    public ProdutoResponse buscarProduto(Long produtoId, Map<String, String> errors) {
        log.info("EXECUTANDO SERVICE-PRODUTO BUSCAR PRODUTO");

        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException("Houve um erro ao tentar buscar o produto", ProdutoNotFoundException.class, errors);

        return produtoMapper.produtoToResponse(produtoEntity.get());
    }
}
