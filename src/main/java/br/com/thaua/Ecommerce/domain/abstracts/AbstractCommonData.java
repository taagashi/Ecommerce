package br.com.thaua.Ecommerce.domain.abstracts;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractCommonData extends AbstractEntity{
    private String name;
    @Email
    private String email;

}
