package br.com.thaua.Ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailMessageService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remetente;

    private void enviarEmails(String assunto, String texto, String destinatario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(texto);
        message.setFrom(remetente);

        mailSender.send(message);
    }

    public void registroDeUsuario(String nome, String destinatario) {
        enviarEmails("Registro Ecommerce", "Parabéns " + nome + ", você acaba de se registrar no nosso Ecommerce :)", destinatario);
    }
}
