package br.com.thaua.Ecommerce.services.validators;

import br.com.thaua.Ecommerce.domain.entity.*;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import br.com.thaua.Ecommerce.repositories.CodigoVerificacaoRepository;
import br.com.thaua.Ecommerce.repositories.UsersRepository;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidationService {

    private final UsersRepository usersRepository;
    private final CodigoVerificacaoRepository codigoVerificacaoRepository;

    public void validarCadastroEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR CADASTRO ENDERECO");
        if (usersEntity.getEndereco() != null) {
            errors.put("Endereço", "Endereço já cadastrado");
        }
    }

    public void validarSiglaEstado(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR SIGLA ESTADO");
        if(Arrays.stream(Estado.values()).noneMatch(estado -> estado.toString().equals(enderecoRequest.getEstado()))) {
            errors.put("Estado", "Sigla de estado inválido");
        }
    }

    public void validarEnderecoNaoExistente(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR ENDERECO NAO EXISTENTE");
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarDelecaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR DELECAO ENDERECO");
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarAtualizacaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR ATUALIZACAO ENDERECO");
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }


    public void validarTelefone(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR TELEFONE");
        if (usersEntity.getTelefone() == null) {
            errors.put("Telefone", "Telefone não cadastrado");
        }
    }

    public void validarCNPJ(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR CNPJ");
        if(usersEntity.getFornecedor().getCnpj() == null) {
            errors.put("CNPJ", "CNPJ está não cadastrado");
        }
    }

    public void validarEstoque(ProdutoRequest produtoRequest, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR ESTOQUE");
        if(produtoRequest.getEstoque() <= 0) {
            errors.put("Estoque", "Não é possível um produto com estoque zerado ou inferior");
        }
    }

    public void validarPreco(ProdutoRequest produtoRequest, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR PRECO");
        if (produtoRequest.getPreco().floatValue() <= 0) {
            errors.put("Preço", "Não é possivel cadastrar um produto com preço zerado ou inferior");
        }
    }


    public void validarQuantidadePedido(ItemPedidoEntity itemPedidoEntity, ProdutoEntity produtoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR QUANTIDADE PEDIDO");
        if(itemPedidoEntity.getQuantidade() <= 0 || itemPedidoEntity.getQuantidade() > produtoEntity.getEstoque()) {
            errors.put(produtoEntity.getNome(), "Quantidade acima do estoque ou igual a 0 para este produto");
        }
    }

    public void validarCpf(UsersEntity usersEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR CPF");
        if(usersEntity.getCliente().getCpf() == null) {
            errors.put("CPF", "CPF não cadastrado");
        }
    }

    public void validarExistenciaEntidade(Object object, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR EXISTENCIA ENTIDADE");
        if(object == null) {
            errors.put("Falha de busca", "Item não encontrado");
        }
    }

    public void validarPagamentoPedido(PedidoEntity pedidoEntity, BigDecimal valorPedido, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR PAGAMENTO PEDIDO");
        if(valorPedido.intValue() < pedidoEntity.getValorPedido().intValue()) {
            errors.put("Valor", "O valor fornecido é invalido para a compra do pedido");
        }
    }

    public void validarStatusPedidoPagamento(PedidoEntity pedidoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR STATUS PEDIDO PAGAMENTO");
        if(pedidoEntity.getStatusPedido() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            errors.put("Status", "O pedido já foi pago");
        }
    }

    public void validarStatusPedidoEditar(PedidoEntity pedidoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR STATUS PEDIDO EDITAR");
        if(pedidoEntity.getStatusPedido() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            errors.put("Status", "Só é possível editar um pedido que ainda não foi pago");
        }
    }

    public void validarDemandaProduto(ProdutoEntity produtoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR DEMANDA PRODUTO");
        if(produtoEntity.getItensPedidos().isEmpty()) {
            errors.put("Demanda", "Não existe nenhuma demanda para esse produto");
        }
    }

    public void validarStatusPedidoEnviar(List<ItemPedidoEntity> itensPedidosEnviar, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR STATUS PEDIDO ENVIAR");
        if(itensPedidosEnviar.isEmpty()) {
            errors.put("Pedido", "Não é possível enviar o produto porque o pedido ainda nao foi pago");
        }
    }

    public UsersEntity validarExistenciaEmail(String email, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR EXISTENCIA EMAIL");
        UsersEntity usersEntity = usersRepository.findByEmail(email);

        if(usersEntity == null) {
            errors.put("Email", "Email não foi encontrado");
        }

        return usersEntity;
    }

    public CodigoVerificacaoEntity validarCodigoVerificacao(int codigo, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR CODIGO VERIFICACAO");
        CodigoVerificacaoEntity codigoVerificacao = codigoVerificacaoRepository.findByCodigo(codigo);

        if(codigoVerificacao == null) {
            errors.put("Código", "Código de verificação inválido");
        }

        return codigoVerificacao;
    }

    public void validarStatusPedidoAdicionarProduto(PedidoEntity pedidoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR STATUS PEDIDO ADICIONAR PRODUTO");
        if(pedidoEntity.getStatusPedido() == StatusPedido.ENVIADO || pedidoEntity.getStatusPedido() == StatusPedido.CANCELADO) {
            errors.put("Status", "Status do pedido inválido para adicionar produto");
        }
    }

    public void validarStatusPedidoDeletar(PedidoEntity pedidoEntity, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - VALIDAR STATUS PEDIDO DELETAR");
        if(pedidoEntity.getStatusPedido() == StatusPedido.PAGO || pedidoEntity.getStatusPedido() == StatusPedido.PAGO_ENVIANDO) {
            errors.put("Status", "Status do pedido inválido para deletar item pedido");
        }
    }

    public void analisarException(String message, Class<? extends RuntimeException> typeException, Map<String, String> errors) {
        log.info("VALIDATION SERVICE - ANALISAR EXCEPTION");
        if(!errors.isEmpty()) {
            try {
                Constructor<? extends RuntimeException> constructor = typeException.getConstructor(String.class, Map.class);

                throw constructor.newInstance(message, errors);

            }catch (ReflectiveOperationException e) {
                throw new RuntimeException("Erro ao lançar exceção personalizada", e);
            }
        }
    }

    public void validarStatusPedidoListagem(String statusPedido, Map<String, String> errors) {
        if(Arrays.stream(StatusPedido.values()).noneMatch(status -> status.toString().equals(statusPedido))) {
            errors.put("Status", "O Status digitado não existe");
        }
    }
}
