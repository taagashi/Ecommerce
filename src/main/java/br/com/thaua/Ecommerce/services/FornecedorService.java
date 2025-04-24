package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorSaldoResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorViewProfileResponse;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.ItemPedidoNotFoundException;
import br.com.thaua.Ecommerce.exceptions.ProdutoException;
import br.com.thaua.Ecommerce.exceptions.ProdutoNotFoundException;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.mappers.ItemPedidoMapper;
import br.com.thaua.Ecommerce.mappers.PaginaMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.*;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import br.com.thaua.Ecommerce.services.validators.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoMapper itemPedidoMapper;

//    @CachePut(value = "forneceedores", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername()")
    @Transactional
    public FornecedorResponse atualizarCNPJeTelefone(FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.getFornecedor().setCnpj(fornecedorCNPJTelefoneRequest.getCnpj());
        usersEntity.setTelefone(fornecedorCNPJTelefoneRequest.getTelefone());


        log.info("EXECUTANDO SERVICE-FORNECEDOR ATUALIZAR CNPJ E TELEFONE");
        return fornecedorMapper.FornecedorToResponse(usersRepository.save(usersEntity).getFornecedor());
    }

//    @CacheEvict(value = "produtos-Fornecedor", allEntries = true)
    @Transactional
    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        validationService.validarCNPJ(usersEntity, errors);
        validationService.validarTelefone(usersEntity, errors);
        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro ao tentar cadastrar o produto " + produtoRequest.getNome(), ProdutoException.class, errors);

        ProdutoEntity produtoEntity = produtoMapper.produtoRequestToEntity(produtoRequest);
        produtoEntity.setFornecedor(usersEntity.getFornecedor());


        log.info("EXECUTANDO SERVICE-FORNECEDOR CADASTRAR PRODUTO");
        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity));
    }

//    @Cacheable(value = "produtos-Fornecedor", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUsername() + '_' + #pageable.getPageNumber() + '_' + #pageable.getPageSize()")
    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        log.info("EXECUTANDO SERVICE-FORNECEDOR EXIBIR PRODUTO");
        return paginaMapper.toPagina(produtoRepository.findAllByFornecedorId(usersEntity.getId(), pageable).map(produtoMapper::produtoToResponse));
    }

//    @Cacheable("produtos-Fornecedor")
    public ProdutoResponse buscarProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao buscar produto", ProdutoNotFoundException.class, errors);


        log.info("EXECUTANDO SERVICE-FORNECEDOR BUSCAR PRODUTO");
        return produtoMapper.produtoToResponse(produtoEntity.get());
    }

//    @CacheEvict(value = "produtos-Fornecedor", allEntries = true)
    @Transactional
    public ProdutoResponse atualizarProduto(Long produtoid, ProdutoRequest produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoid, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar atualizar produto", ProdutoNotFoundException.class, errors);

        produtoEntity.get().setNome(produtoRequest.getNome());
        produtoEntity.get().setPreco(produtoRequest.getPreco());
        produtoEntity.get().setDescricao(produtoRequest.getDescricao());
        produtoEntity.get().setEstoque(produtoRequest.getEstoque());


        log.info("EXECUTANDO SERVICE-FORNECEDOR ATUALIZAR PRODUTO");
        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity.get()));
    }

//    @CachePut(value = "categorias-Produtos", key = "#categoriaId")
    @Transactional
    public ProdutoResponse adicionarProdutoACategoria(Long categoriaId, Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);

        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar adicionar produto na categoria", ProdutoNotFoundException.class, errors);

        categoriaEntity.get().getProdutos().add(produtoEntity.get());
        produtoEntity.get().getCategorias().add(categoriaEntity.get());

        categoriaRepository.save(categoriaEntity.get());
        produtoRepository.save(produtoEntity.get());


        log.info("EXECUTANDO SERVICE-FORNECEDOR ADICIONAR PRODUTO A CATEGORIA");
        return produtoMapper.produtoToResponse(produtoEntity.get());
    }

//    @CacheEvict(value = "produtos-Fornecedor", allEntries = true)
    @Transactional
    public ProdutoResponse atualizarEstoqueProduto(Long produtoId, ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao atualizar estoque do produto", ProdutoNotFoundException.class, errors);

        produtoEntity.get().setEstoque(produtoNovoEstoqueRequest.getNovaQuantidade());


        log.info("EXECUTANDO SERVICE-FORNECEDOR ATUALIZAR ESTOQUE PRODUTO");
        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity.get()));
    }

//    PRECISO DAR UMA REVISADA MELHOR NESSE PARTE DE CODIGO
//    @CacheEvict(value = "produtos-Fornecedor", allEntries = true)
    @Transactional
    public String removerProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar remover produto", ProdutoNotFoundException.class, errors);

        for(CategoriaEntity categoria : produtoEntity.get().getCategorias()) {
            categoria.getProdutos().remove(produtoEntity);
            categoriaRepository.save(categoria);
        }
        produtoRepository.save(produtoEntity.get());
        produtoRepository.delete(produtoEntity.get());


       log.info("EXECUTANDO SERVICE-FORNECEDOR REMOVER PRODUTO");
        return produtoEntity.get().getNome() + " foi removido com sucesso, " + usersEntity.getName();
    }


//    @CacheEvict(value = "categorias-Produtos", allEntries = true)
    @Transactional
    public String removerProdutoDeCategoria(Long categoriaId, Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar remover produto de categoria", ProdutoNotFoundException.class, errors);

        categoriaEntity.get().getProdutos().remove(produtoEntity.get());
        produtoEntity.get().getCategorias().remove(categoriaEntity.get());
        categoriaRepository.save(categoriaEntity.get());
        produtoRepository.save(produtoEntity.get());


        log.info("EXECUTANDO SERVICE-FORNECEDOR REMOVER PRODUTO CATEGORIA");
        return produtoEntity.get().getNome() + " foi removido com sucesso da categoria" + categoriaEntity.get().getNome();
    }

    @Transactional
    public String enviarProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.validarDemandaProduto(produtoEntity.orElse(null), errors);

        List<ItemPedidoEntity> itensPedidosEnviar = produtoEntity.get().getItensPedidos()
                .stream()
                .filter(item -> item.getPedido().getStatusPedido() == StatusPedido.PAGO)
                .toList();

        validationService.validarStatusPedidoEnviar(itensPedidosEnviar, errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar enviar produto", ItemPedidoNotFoundException.class, errors);

        for(ItemPedidoEntity item : itensPedidosEnviar) {
            usersEntity.getFornecedor().setSaldo(usersEntity.getFornecedor().getSaldo().add(item.getValorTotal()));
            item.setStatusItemPedido(StatusItemPedido.ENVIADO);
            usersEntity.getFornecedor().setProdutosEnviados(usersEntity.getFornecedor().getProdutosEnviados() + 1);
            item.getPedido().setStatusPedido(StatusPedido.PAGO_ENVIANDO);
        }

        produtoRepository.save(produtoEntity.get());
        usersRepository.save(usersEntity);
        return produtoEntity.get().getNome() + " foi enviado com sucesso para os clientes";
    }

    public List<ItemPedidoResponse> listarDemandaProduto(Long produtoId, Map<String, String> errors) {
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors);
        validationService.validarDemandaProduto(produtoEntity.orElse(null),
                errors);
        validationService.analisarException("Houve um erro ao tentar listar demanda de produto", ProdutoException.class, errors);

        return produtoEntity.get().getItensPedidos().stream().filter(item -> item.getStatusItemPedido() == StatusItemPedido.PROCESSANDO).map(itemPedidoMapper::toItemPedidoResponse).toList();
    }

    public FornecedorSaldoResponse exibirSaldoAtual() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        return fornecedorMapper.fornecedorEntityToFornecedorSaldoResponse(usersEntity.getFornecedor());
    }

    public FornecedorViewProfileResponse exibirPerfil() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        return fornecedorMapper.fornecedorEntityToFornecedorViiewProfileResponse(usersEntity.getFornecedor());
    }
}
