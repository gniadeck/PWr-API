package dev.wms.pwrapi.dto.library.deserialization;

public record LibraryResultProperties(LibraryDisplayProperties display, LibraryAdditionalProperties addata,
                                      LibraryResourceProperties delivery) {
}
