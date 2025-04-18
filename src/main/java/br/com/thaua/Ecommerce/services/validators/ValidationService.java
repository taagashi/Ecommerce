package br.com.thaua.Ecommerce.services.validators;

import br.com.thaua.Ecommerce.domain.entity.ItemPedidoEntity;
import br.com.thaua.Ecommerce.domain.entity.PedidoEntity;
import br.com.thaua.Ecommerce.domain.entity.ProdutoEntity;
import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import br.com.thaua.Ecommerce.domain.enums.StatusPedido;
import br.com.thaua.Ecommerce.dto.endereco.EnderecoRequest;
import br.com.thaua.Ecommerce.dto.itemPedido.ItemPedidoRequest;
import br.com.thaua.Ecommerce.dto.produto.ProdutoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ValidationService {

    public void validarCadastroEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if (usersEntity.getEndereco() != null) {
            errors.put("Endereço", "Endereço já cadastrado");
        }
    }

    public void validarSiglaEstado(EnderecoRequest enderecoRequest, Map<String, String> errors) {
        if(Arrays.stream(Estado.values()).noneMatch(estado -> estado.toString().equals(enderecoRequest.getEstado()))) {
            errors.put("Estado", "Sigla de estado inválido");
        }
    }

    public void validarEnderecoNaoExistente(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarDelecaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }

    public void validarAtualizacaoEndereco(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getEndereco() == null) {
            errors.put("Endereço", "Endereço não cadastrado");
        }
    }


    public void validarTelefone(UsersEntity usersEntity, Map<String, String> errors) {
        if (usersEntity.getTelefone() == null) {
            errors.put("Telefone", "Telefone não cadastrado");
        }
    }

    public void validarCNPJ(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getFornecedor().getCnpj() == null) {
            errors.put("CNPJ", "CNPJ está não cadastrado");
        }
    }

    public void validarEstoque(ProdutoRequest produtoRequest, Map<String, String> errors) {
        if(produtoRequest.getEstoque() <= 0) {
            errors.put("Estoque", "Não é possível um produto com estoque zerado ou inferior");
        }
    }

    public void validarPreco(ProdutoRequest produtoRequest, Map<String, String> errors) {
        if (produtoRequest.getPreco().floatValue() <= 0) {
            errors.put("Preço", "Não é possivel cadastrar um produto com preço zerado ou inferior");
        }
    }


    public void validarQuantidadePedido(List<ItemPedidoRequest> itemPedidoRequest, Map<String, String> errors) {
        if(itemPedidoRequest.stream().anyMatch(itemPedido -> itemPedido.getQuantidade() <= 0)) {
            errors.put("Quantidade", "Quandiade inválida");
        }
    }

    public void validarCpf(UsersEntity usersEntity, Map<String, String> errors) {
        if(usersEntity.getCliente().getCpf() == null) {
            errors.put("CPF", "CPF não cadastrado");
        }
    }

    public void validarExistenciaEntidade(Object object, Map<String, String> errors) {
        if(object == null) {
            errors.put("Falha de busca", "Item não encontrado");
        }
    }

    public void analisarException(String message, Class<? extends RuntimeException> typeException, Map<String, String> errors) {
        if(!errors.isEmpty()) {
            try {
                Constructor<? extends RuntimeException> constructor = typeException.getConstructor(String.class, Map.class);

                throw constructor.newInstance(message, errors);

            }catch (ReflectiveOperationException e) {
                throw new RuntimeException("Erro ao lançar exceção personalizada", e);
            }
        }
    }

    public void validarPagamentoPedido(PedidoEntity pedidoEntity, BigDecimal valorPedido, Map<String, String> errors) {
        if(valorPedido.intValue() < pedidoEntity.getValorPedido().intValue()) {
            errors.put("Valor", "O valor fornecido é invalido para a compra do pedido");
        }
    }

    public void validarStatusPedidoPagamento(PedidoEntity pedidoEntity, Map<String, String> errors) {
        if(pedidoEntity.getStatusPedido() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            errors.put("Status", "O pedido já foi pago");
        }
    }

    public void validarDemandaProduto(ProdutoEntity produtoEntity, Map<String, String> errors) {
        if(produtoEntity.getItensPedidos().isEmpty()) {
            errors.put("Demanda", "Não existe nenhuma demanda para esse produto");
        }
    }

    public void validarStatusPedidoEnviar(List<ItemPedidoEntity> itensPedidosEnviar, Map<String, String> errors) {
        if(itensPedidosEnviar.isEmpty()) {
            errors.put("Pedido", "Não é possível enviar o produto porque o pedido ainda nao foi pago");
        }
    }
}
