package io.swagger.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SenderEmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * Sends text email in html format to the user
     *
     * @param to   The recipient of the message
     * @param link The link to confirm registering
     */
    @Override
    @Async
    public void send(String to, String link) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    String.valueOf(StandardCharsets.UTF_8));
            helper.setText("Please confirm your registration using the <a href=" + link + ">link</a>", true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }
}
