package dev.wms.pwrapi.service.email;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;

    private final JavaMailSender mailSender;

    @Async
    @Override
    @SneakyThrows
    public void sendMessage(String receiver, String subject, String body, boolean isHtml) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom(EMAIL_FROM);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(body, isHtml);

        mailSender.send(mimeMessage);
    }
}
