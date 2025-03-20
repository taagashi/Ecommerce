    package br.com.thaua.Ecommerce.domain.entity;

    import br.com.thaua.Ecommerce.domain.abstracts.AbstractCommonData;
    import br.com.thaua.Ecommerce.domain.abstracts.AbstractEntity;
    import jakarta.persistence.Entity;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.OneToOne;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    public class Admin extends AbstractCommonData {
        private Integer contasBanidas;

        @OneToOne(mappedBy = "admin")
        private Users users;

        private LocalDateTime ultimoAcesso;
    }

