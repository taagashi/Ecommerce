package br.com.thaua.Ecommerce.domain.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@MappedSuperclass
@Getter
@Setter
public class AbstractCommonData {
    private String name;

    @Id
    @Column(unique = true, nullable = false)
    private String email;
}
