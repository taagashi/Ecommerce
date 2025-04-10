package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.CategoriaEntity;
import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.ProdutoException;
import br.com.thaua.Ecommerce.exceptions.ProdutoNotFoundException;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.CategoriaRepository;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FornecedorService {
    private final UsersRepository usersRepository;
    private final FornecedorMapper fornecedorMapper;
    private final ProdutoMapper produtoMapper;
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ValidationService validationService;
    private final PaginaMapper paginaMapper;

    public FornecedorResponse atualizarCNPJeTelefone(FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.getFornecedor().setCnpj(fornecedorCNPJTelefoneRequest.getCnpj());
        usersEntity.setTelefone(fornecedorCNPJTelefoneRequest.getTelefone());

        return fornecedorMapper.FornecedorToResponse(usersRepository.save(usersEntity).getFornecedor());
    }

    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarCNPJ(usersEntity, errors);
        validationService.validarTelefone(usersEntity, errors);
        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro ao tentar cadastrar o produto " + produtoRequest.getNome(), ProdutoException.class, errors);

        ProdutoEntity produtoEntity = produtoMapper.produtoRequestToEntity(produtoRequest);
        produtoEntity.setFornecedor(usersEntity.getFornecedor());

        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity));
    }

    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        return paginaMapper.toPagina(produtoRepository.findAllByFornecedorId(usersEntity.getId(), pageable).map(produtoMapper::produtoToResponse));
    }

    public ProdutoResponse buscarProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao buscar produto", ProdutoNotFoundException.class, errors);

        return produtoMapper.produtoToResponse(produtoEntity.get());
    }

    public ProdutoResponse atualizarProduto(Long produtoid, ProdutoRequest produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoid, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar atualizar produto", ProdutoNotFoundException.class, errors);

        produtoEntity.get().setNome(produtoRequest.getNome());
        produtoEntity.get().setPreco(produtoRequest.getPreco());
        produtoEntity.get().setDescricao(produtoRequest.getDescricao());
        produtoEntity.get().setEstoque(produtoRequest.getEstoque());

        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity.get()));
    }

    public ProdutoResponse adicionarProdutoACategoria(Long categoriaId, Long produtoId) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId()).get();

        categoriaEntity.getProdutos().add(produtoEntity);
        produtoEntity.getCategorias().add(categoriaEntity);

        categoriaRepository.save(categoriaEntity);
        produtoRepository.save(produtoEntity);

        return produtoMapper.produtoToResponse(produtoEntity);
    }

    public ProdutoResponse atualizarEstoqueProduto(Long produtoId, ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId()).get();
        produtoEntity.setEstoque(produtoNovoEstoqueRequest.getNovaQuantidade());

        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity));
    }

//    PRECISO DAR UMA REVISADA MELHOR NESSE PARTE DE CODIGO
    public String removerProduto(Long produtoId) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId()).get();

        for(CategoriaEntity categoria : produtoEntity.getCategorias()) {
            categoria.getProdutos().remove(produtoEntity);
            categoriaRepository.save(categoria);
        }
        produtoRepository.save(produtoEntity);
        produtoRepository.delete(produtoEntity);

        return produtoEntity.getNome() + " foi removido com sucesso, " + usersEntity.getName();
    }

    public String removerProdutoDeCategoria(Long categoriaId, Long produtoId) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId()).get();
        CategoriaEntity categoriaEntity = categoriaRepository.findById(categoriaId).get();

        categoriaEntity.getProdutos().remove(produtoEntity);
        produtoEntity.getCategorias().remove(categoriaEntity);
        categoriaRepository.save(categoriaEntity);
        produtoRepository.save(produtoEntity);

        return produtoEntity.getNome() + " foi removido com sucesso da categoria" + categoriaEntity.getNome();
    }
}
