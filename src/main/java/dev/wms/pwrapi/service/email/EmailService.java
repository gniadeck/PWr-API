package dev.wms.pwrapi.service.email;

import lombok.SneakyThrows;


public interface EmailService {

    @SneakyThrows
    void sendMessage(String receiver, String subject, String body, boolean isHtml);
}
