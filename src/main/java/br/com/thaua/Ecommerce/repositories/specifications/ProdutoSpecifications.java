package br.com.thaua.Ecommerce.repositories.specifications;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.pagina.GerarPaginacao;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProdutoSpecifications {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public Page<ProdutoEntity> buscarComFiltros(BigDecimal min, BigDecimal max, Pageable pageable) {
        if(min != null && max != null) {
            return produtoRepository.findAllByPrecoBetween(min, max, pageable);
        } else if(min != null) {
            return produtoRepository.findAllByPrecoGreaterThanEquals(min, pageable);
        } else if(max != null) {
            return produtoRepository.findAllByPrecoLessThanEquals(max, pageable);
        } else {
            return produtoRepository.findAll(pageable);
        }
    }

    public Page<ProdutoEntity> buscarComFiltros(BigDecimal min, BigDecimal max, Pageable pageable, Long fornecedorID) {
        return null;
    }
}
