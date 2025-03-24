package br.com.thaua.Ecommerce.services;

import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorCNPJTelefoneRequest;
import br.com.thaua.Ecommerce.dto.fornecedor.FornecedorResponse;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoResponse;
import br.com.thaua.Ecommerce.mappers.FornecedorMapper;
import br.com.thaua.Ecommerce.mappers.ProdutoMapper;
import br.com.thaua.Ecommerce.repositories.ProdutoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import br.com.thaua.Ecommerce.services.returnTypeUsers.ExtractTypeUserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FornecedorService {
    private final UsersRepository usersRepository;
    private final FornecedorMapper fornecedorMapper;
    private final ProdutoMapper produtoMapper;
    private final ProdutoRepository produtoRepository;

    public FornecedorResponse atualizarCNPJeTelefone(FornecedorCNPJTelefoneRequest fornecedorCNPJTelefoneRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        usersEntity.getFornecedor().setCnpj(fornecedorCNPJTelefoneRequest.getCnpj());
        usersEntity.getFornecedor().setTelefone(fornecedorCNPJTelefoneRequest.getTelefone());

        return fornecedorMapper.FornecedorToResponse(usersRepository.save(usersEntity).getFornecedor());
    }

    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();

        if(usersEntity.getFornecedor().getCnpj() == null || usersEntity.getEndereco() == null || usersEntity.getFornecedor().getTelefone() == null) {
            throw new RuntimeException(usersEntity.getName() + ", é necessário que você preencha informações importantes, como CNPJ, endereco e telefone para cadastrar algum produto");
        }
        ProdutoEntity produtoEntity = produtoMapper.produtoRequestToEntity(produtoRequest);
        produtoEntity.setFornecedor(usersEntity.getFornecedor());

        return produtoMapper.produtoToResponse(produtoRepository.save(produtoEntity));
    }

    public List<ProdutoResponse> exibirProdutos() {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        return produtoMapper.produtoToResponseList(usersEntity.getFornecedor().getProduto());
    }

    public ProdutoResponse buscarProduto(Long produtoId) {
        UsersEntity usersEntity = ExtractTypeUserContextHolder.extractUser();
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndFornecedorId(produtoId, usersEntity.getId()).get();

        return produtoMapper.produtoToResponse(produtoEntity);
    }
}
