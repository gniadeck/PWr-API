package dev.wms.pwrapi.dto.isjsosdown;

import lombok.Builder;
import lombok.Data;

@Builder
public record ServiceDTO(String name, String url) {
}
