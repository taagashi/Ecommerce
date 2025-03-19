package br.com.thaua.Ecommerce.domain.abstracts;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractCommonData extends AbstractEntity{
    private String name;
    private String email;
}
