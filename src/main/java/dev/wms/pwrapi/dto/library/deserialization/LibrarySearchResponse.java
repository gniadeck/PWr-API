package dev.wms.pwrapi.dto.library.deserialization;

import java.util.List;

public record LibrarySearchResponse(List<LibraryResultResponse> docs) {
}
