package dev.wms.pwrapi.dto.library;

import lombok.Builder;

import java.util.List;

@Builder
public record LibraryTitle(String title, String description, String category, String source, List<String> languages, List<String> subjects,
                           String creationDate, String author, String publisher, String place, List<String> issns, String detailsUrl, List<String> pubs,
                           List<String> genres, List<String> identifiers, List<String> isbns, List<String> rights) {
}
