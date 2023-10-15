package dev.wms.pwrapi.dto.library.converters;

import dev.wms.pwrapi.dto.library.LibraryTitle;
import dev.wms.pwrapi.dto.library.deserialization.LibraryResourceProperties;
import dev.wms.pwrapi.dto.library.deserialization.LibraryResultResponse;
import dev.wms.pwrapi.dto.library.deserialization.LibrarySearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.wms.pwrapi.utils.common.JsonParsingUtils.collectionToString;

@Service
public class LibraryDtoConverter {

    public List<LibraryTitle> toLibraryTitle(LibrarySearchResponse response){
        return response.docs().stream()
                .map(this::toLibraryTitle)
                .toList();
    }

    private LibraryTitle toLibraryTitle(LibraryResultResponse response){
        return LibraryTitle.builder()
                .title(collectionToString(response.pnx().display().title()))
                .description(collectionToString(response.pnx().display().description()))
                .category(collectionToString(response.pnx().display().type()))
                .source(collectionToString(response.pnx().display().source()))
                .languages(response.pnx().display().language())
                .subjects(response.pnx().display().subject())
                .creationDate(collectionToString(response.pnx().display().creationdate()))
                .author(collectionToString(response.pnx().display().creator()))
                .publisher(collectionToString(response.pnx().display().publisher()))
                .place(collectionToString(response.pnx().display().place()))
                .issns(response.pnx().addata().issn())
                .detailsUrl(Optional.ofNullable(response.pnx().delivery()).map(LibraryResourceProperties::almaOpenurl).orElse(""))
                .pubs(response.pnx().addata().pub())
                .genres(response.pnx().addata().genre())
                .identifiers(response.pnx().display().identifier())
                .isbns(response.pnx().addata().isbn())
                .rights(response.pnx().display().rights())
                .build();
    }

}
