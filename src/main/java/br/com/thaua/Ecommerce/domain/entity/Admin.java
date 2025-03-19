    package br.com.thaua.Ecommerce.domain.entity;

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
    public class Admin extends AbstractEntity {
        private Integer contasBanidas;

        @OneToOne
        @JoinColumn(name = "user_id")
        private Users users;

        private LocalDateTime ultimoAcesso;
    }

