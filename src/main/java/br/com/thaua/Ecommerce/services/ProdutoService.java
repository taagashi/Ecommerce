package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.specifications.ProdutoSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ProdutoSpecifications produtoSpecifications;
    private final PaginaMapper paginaMapper;

    public ProdutoCategoriaResponse exibirCategoriasDeProduto(Long produtoId) {
        ProdutoEntity produtoEntity = produtoRepository.findById(produtoId).get();

        return produtoMapper.toProdutoCategoriaResponse(produtoEntity);
    }

    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable, BigDecimal min, BigDecimal max) {
        return paginaMapper.toPagina(produtoSpecifications.buscarComFiltros(min, max, pageable).map(produtoMapper::produtoToResponse));
    }
}
