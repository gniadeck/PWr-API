package dev.wms.pwrapi.dto.library.deserialization;

import java.util.List;

public record LibraryAdditionalProperties(List<String> stitle, List<String> addTitle, List<String> date,
                                          List<String> issn, List<String> pub, List<String> format,
                                          List<String> genre, List<String> isbn) {
}
