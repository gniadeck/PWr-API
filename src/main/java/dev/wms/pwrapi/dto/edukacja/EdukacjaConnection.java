package dev.wms.pwrapi.dto.edukacja;

import lombok.Builder;

@Builder
public record EdukacjaConnection(String sessionToken, String webToken, String jsessionid) {
}
