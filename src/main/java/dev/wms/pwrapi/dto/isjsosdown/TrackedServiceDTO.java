package dev.wms.pwrapi.dto.isjsosdown;

import lombok.NonNull;

public record TrackedServiceDTO(Long id, @NonNull String name, @NonNull String url, @NonNull boolean isActive) {

}
