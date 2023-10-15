package dev.wms.pwrapi.dto.library.deserialization;

import java.util.List;

public record LibraryDisplayProperties(List<String> source, List<String> type, List<String> language,
                                       List<String> title, List<String> subject, List<String> creationdate,
                                       List<String> creator, List<String> publisher, List<String> place,
                                       List<String> description, List<String> rights, List<String> identifier) {
}
