package br.com.thaua.Ecommerce.domain.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractCommonData extends AbstractEntity{
    private String name;

    @Column(unique = true)
    private String email;
}
