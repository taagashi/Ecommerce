package br.com.thaua.Ecommerce.domain.entity;

import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
import br.com.thaua.Ecommerce.domain.enums.Estado;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnderecoEntity extends AbstractEntity {
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;

    @Enumerated(EnumType.STRING)
    private Estado estado;
    private String cep;

    @OneToOne(mappedBy = "endereco")
    private UsersEntity users;

}
