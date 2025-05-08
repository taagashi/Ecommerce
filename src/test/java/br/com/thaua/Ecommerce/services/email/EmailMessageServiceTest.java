package br.com.thaua.Ecommerce.services.email;

import br.com.thaua.Ecommerce.services.EmailMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailMessageServiceTest {
    @InjectMocks
    private EmailMessageService emailMessageService;

    @Mock
    private JavaMailSender javaMailSender;

    private String destinatario;

    @BeforeEach
    public void setUp() {
        destinatario = "taagashi";
    }

    @DisplayName("Deve registrar usuario com sucesso")
    @Test
    public void testRegistroUsuarioSucesso() {
        String nome = "envio de email";

        emailMessageService.registroDeUsuario(nome, destinatario);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @DisplayName("Deve retornar codigo de redefinição de senha")
    @Test
    public void testGerarCodigoRedefinirSenha() {
        emailMessageService.gerarCodigoRedefinirSenha(destinatario);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
