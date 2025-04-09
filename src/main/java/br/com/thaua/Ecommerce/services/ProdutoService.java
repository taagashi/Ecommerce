package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.pagina.GerarPaginacao;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.GenericArrayType;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoCategoriaResponse exibirCategoriasDeProduto(Long produtoId) {
        ProdutoEntity produtoEntity = produtoRepository.findById(produtoId).get();

        return produtoMapper.toProdutoCategoriaResponse(produtoEntity);
    }

    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable, BigDecimal min, BigDecimal max) {
        if(min != null && max != null) {
            return GerarPaginacao.gerarPaginacao(produtoRepository.findAllByPrecoBetween(min, max, pageable).map(produtoMapper::produtoToResponse));
        } else if(min != null) {
            return GerarPaginacao.gerarPaginacao(produtoRepository.findAllByPrecoGreaterThanEquals(min, pageable).map(produtoMapper::produtoToResponse));
        } else if(max != null) {
            return GerarPaginacao.gerarPaginacao(produtoRepository.findAllByPrecoLessThanEquals(max, pageable).map(produtoMapper::produtoToResponse));
        } else {
            return GerarPaginacao.gerarPaginacao(produtoRepository.findAll(pageable).map(produtoMapper::produtoToResponse));
        }
    }
}
