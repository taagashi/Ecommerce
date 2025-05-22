package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.StatusItemPedido;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorSaldoResponse;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoResponse;
import br.com.thaua.Ecommerce.dto.pagina.Pagina;
import br.com.thaua.Ecommerce.dto.produto.ProdutoNovoEstoqueRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.exceptions.*;
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
    private final ItemPedidoMapper itemPedidoMapper;
    private final ExtractTypeUserContextHolder extractTypeUserContextHolder;

    @Transactional
    public FornecedorResponse atualizarCNPJeTelefone(FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        usersEntity.getFornecedor().setCnpj(fornecedorCNPJTelefoneRequest.getCnpj());
        usersEntity.setTelefone(fornecedorCNPJTelefoneRequest.getTelefone());


        log.info("SERVICE FORNECEDOR - ATUALIZAR CNPJ E TELEFONE");
        return fornecedorMapper.fornecedorToResponse(usersRepository.save(usersEntity).getFornecedor());
    }

    @Transactional
    public List<ProdutoResponse> cadastrarProduto(List<ProdutoRequest> produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        validationService.validarCNPJ(usersEntity, errors);
        validationService.validarTelefone(usersEntity, errors);
        validationService.validarEnderecoNaoExistente(usersEntity, errors);
        validationService.analisarException(usersEntity.getName() + ", houve um erro ao tentar cadastrar o produto", CadastrarProdutoException.class, errors);

        List<ProdutoEntity> produtoEntity = produtoMapper.produtoRequestToProdutoEntity(produtoRequest);
        produtoEntity.forEach(produto -> produto.setFornecedor(usersEntity.getFornecedor()));

        log.info("SERVICE FORNECEDOR - CADASTRAR PRODUTO");
        return produtoMapper.produtoToResponseList(produtoRepository.saveAll(produtoEntity));
    }

    public Pagina<ProdutoResponse> exibirProdutos(Pageable pageable) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        log.info("SERVICE FORNECEDOR - CADASTRAR PRODUTOS");
        return paginaMapper.toPagina(produtoRepository.findAllByFornecedorId(usersEntity.getId(), pageable).map(produtoMapper::produtoToResponse));
    }

    public ProdutoResponse buscarProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.analisarException(usersEntity.getName() + " houve um erro ao buscar produto", ProdutoNotFoundException.class, errors);


        log.info("SERVICE FORNECEDOR - BUSCAR PRODUTO");
        return produtoMapper.produtoToResponse(produtoEntity.get());
    }

    @Transactional
    public ProdutoResponse atualizarProduto(Long produtoid, ProdutoRequest produtoRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoid, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar atualizar produto", ProdutoNotFoundException.class, errors);

        produtoEntity.get().setNome(produtoRequest.getNome());
        produtoEntity.get().setPreco(produtoRequest.getPreco());
        produtoEntity.get().setDescricao(produtoRequest.getDescricao());
        produtoEntity.get().setEstoque(produtoRequest.getEstoque());


        log.info("SERVICE FORNECEDOR - ATUALIZAR PRODUTO");
        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity.get()));
    }

    @Transactional
    public ProdutoResponse adicionarProdutoACategoria(Long categoriaId, Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors, "Categoria");
        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");

        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar adicionar produto na categoria", ProdutoCategoriaException.class, errors);

        categoriaEntity.get().getProdutos().add(produtoEntity.get());
        produtoEntity.get().getCategorias().add(categoriaEntity.get());

        categoriaRepository.save(categoriaEntity.get());
        produtoRepository.save(produtoEntity.get());


        log.info("SERVICE FORNECEDOR - ADICIONAR PRODUTO A CATEGORIA");
        return produtoMapper.produtoToResponse(produtoEntity.get());
    }

    @Transactional
    public ProdutoResponse atualizarEstoqueProduto(Long produtoId, ProdutoNovoEstoqueRequest produtoNovoEstoqueRequest, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.analisarException(usersEntity.getName() + " houve um erro ao atualizar estoque do produto", ProdutoNotFoundException.class, errors);

        produtoEntity.get().setEstoque(produtoNovoEstoqueRequest.getNovaQuantidade());


        log.info("SERVICE FORNECEDOR - ATUALIZAR ESTOQUE PRODUTO");
        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity.get()));
    }

//    PRECISO DAR UMA REVISADA MELHOR NESSE PARTE DE CODIGO
    @Transactional
    public String removerProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar remover produto", ProdutoNotFoundException.class, errors);

        for(CategoriaEntity categoria : produtoEntity.get().getCategorias()) {
            categoria.getProdutos().remove(produtoEntity);
            categoriaRepository.save(categoria);
        }
        produtoRepository.save(produtoEntity.get());
        produtoRepository.delete(produtoEntity.get());


        log.info("SERVICE FORNECEDOR - REMOVER PRODUTO");
        return produtoEntity.get().getNome() + " foi removido com sucesso, " + usersEntity.getName();
    }


    @Transactional
    public String removerProdutoDeCategoria(Long categoriaId, Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId());
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.validarExistenciaEntidade(categoriaEntity.orElse(null), errors, "Categoria");
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar remover produto de categoria", ProdutoCategoriaException.class, errors);

        categoriaEntity.get().getProdutos().remove(produtoEntity.get());
        produtoEntity.get().getCategorias().remove(categoriaEntity.get());
        categoriaRepository.save(categoriaEntity.get());
        produtoRepository.save(produtoEntity.get());


        log.info("SERVICE FORNECEDOR - REMOVER PRODUTO DE CATEGORIA");
        return produtoEntity.get().getNome() + " foi removido com sucesso da categoria" + categoriaEntity.get().getNome();
    }

    @Transactional
    public String enviarProduto(Long produtoId, Map<String, String> errors) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.validarDemandaProduto(produtoEntity.orElse(null), errors);

        List<ItemPedidoEntity> itensPedidosEnviar = produtoEntity.get().getItensPedidos()
                .stream()
                .filter(item -> item.getPedido().getStatusPedido() == StatusPedido.PAGO || item.getPedido().getStatusPedido() == StatusPedido.PAGO_ENVIANDO)
                .toList();

        validationService.validarStatusPedidoEnviar(itensPedidosEnviar, errors);
        validationService.analisarException(usersEntity.getName() + " houve um erro ao tentar enviar produto", EnviarProdutoException.class, errors);

        for(ItemPedidoEntity item : itensPedidosEnviar) {
            usersEntity.getFornecedor().setSaldo(usersEntity.getFornecedor().getSaldo().add(item.getValorTotal()));
            item.setStatusItemPedido(StatusItemPedido.ENVIADO);
            usersEntity.getFornecedor().setProdutosEnviados(usersEntity.getFornecedor().getProdutosEnviados() + 1);
            item.getPedido().setStatusPedido(StatusPedido.PAGO_ENVIANDO);
        }

        produtoEntity.get().setQuantidadeDemanda(produtoEntity.get().getQuantidadeDemanda() - itensPedidosEnviar.size());
        produtoRepository.save(produtoEntity.get());
        usersRepository.save(usersEntity);

        log.info("SERVICE FORNECEDOR - ENVIAR PRODUTO");
        return produtoEntity.get().getNome() + " foi enviado com sucesso para os clientes";
    }

    public List<ItemPedidoResponse> listarDemandaProduto(Long produtoId, Map<String, String> errors) {
        Optional<ProdutoEntity> produtoEntity = produtoRepository.findById(produtoId);

        validationService.validarExistenciaEntidade(produtoEntity.orElse(null), errors, "Produto");
        validationService.validarDemandaProduto(produtoEntity.orElse(null),
                errors);
        validationService.analisarException("Houve um erro ao tentar listar demanda de produto", ProdutoDemandaException.class, errors);

        log.info("SERVICE FORNECEDOR - LISTAR DEMANDA DE PRODUTO");
        return produtoEntity.get().getItensPedidos().stream().filter(item -> item.getStatusItemPedido() == StatusItemPedido.PROCESSANDO).map(itemPedidoMapper::toItemPedidoResponse).toList();
    }

    public FornecedorSaldoResponse exibirSaldoAtual() {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        log.info("SERVICE FORNECEDOR - EXIBIR SALDO ATUAL");
        return fornecedorMapper.fornecedorEntityToFornecedorSaldoResponse(usersEntity.getFornecedor());
    }

    public Pagina<ProdutoResponse> listarProdutosComDemanda(Pageable pageable) {
        UsersEntity usersEntity = extractTypeUserContextHolder.extractUser();

        log.info("SERVICE FORNECEDOR - LISTAR PRODUTOS COM DEMANDA");
        return paginaMapper.toPagina(produtoRepository.findAllByFornecedorIdAndQuantidadeDemandaGreaterThan(usersEntity.getId(), 0, pageable).map(produtoMapper::produtoToResponse));
    }
}
