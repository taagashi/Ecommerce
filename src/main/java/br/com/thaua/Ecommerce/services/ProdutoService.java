package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.dto.produto.ProdutoCategoriaResponse;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoCategoriaResponse exibirCategoriasDeProduto(Long produtoId) {
        ProdutoEntity produtoEntity = produtoRepository.findById(produtoId).get();

        return produtoMapper.toProdutoCategoriaResponse(produtoEntity);
    }
}
