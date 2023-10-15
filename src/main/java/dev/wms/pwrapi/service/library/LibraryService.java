package dev.wms.pwrapi.service.library;

import dev.wms.pwrapi.dao.library.LibraryDAO;
import dev.wms.pwrapi.dto.library.LibraryTitle;
import dev.wms.pwrapi.utils.common.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryDAO libraryDAO;
    public List<LibraryTitle> searchFor(String query, PageRequest pageRequest) {
        return libraryDAO.searchFor(query, pageRequest);
    }

}
