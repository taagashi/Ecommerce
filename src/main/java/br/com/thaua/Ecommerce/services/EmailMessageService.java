package br.com.thaua.Ecommerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailMessageService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remetente;

    public void registroDeUsuario(String nome, String destinatario) {
        log.info("SERVICE EMAIL - REGISTRO DE USUARIO");
        enviarEmails("REGISTRO ECOMMERCE", "Parabéns " + nome + ", você acaba de se registrar no nosso Ecommerce :)", destinatario);
    }

    public int gerarCodigoRedefinirSenha(String destinatario) {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(999999);

        enviarEmails("REDEFINIR SENHA",  "seu código de verificação para mudança de senha é: " + codigo, destinatario);

        log.info("SERVICE EMAIL - GERAR CODIGO DE VERIFICACAO");
        return codigo;
    }

    private void enviarEmails(String assunto, String texto, String destinatario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(texto);
        message.setFrom(remetente);

        log.info("SERVICE EMAIL - ENVIAR EMAIL");
        mailSender.send(message);
    }
}
