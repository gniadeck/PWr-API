package dev.wms.pwrapi.dao.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.wms.pwrapi.dto.library.LibraryTitle;
import dev.wms.pwrapi.dto.library.converters.LibraryDtoConverter;
import dev.wms.pwrapi.dto.library.deserialization.LibrarySearchResponse;
import dev.wms.pwrapi.utils.common.PageRequest;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Request;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LibraryDAO {

    private final LibraryAuthDao authDao;
    private final ObjectMapper objectMapper;
    private final LibraryDtoConverter dtoConverter;

    @SneakyThrows
    public List<LibraryTitle> searchFor(String query, PageRequest pageRequest) {
        var client = authDao.getAnonymousClient();
        Request request = new Request.Builder()
                .url("https://omnis-pwr.primo.exlibrisgroup.com/primaws/rest/pub/pnxs?lang=pl" +
                        "&limit=" + pageRequest.limit() +
                        "&mode=Basic" +
                        "&offset=" + pageRequest.offset() +
                        "&q=any,contains," + query +
                        "&searchInFulltextUserSelection=true" +
                        "&sort=rank" +
                        "&vid=48OMNIS_TUR:48TUR")
                .build();
        var response = objectMapper.readValue(
                new HttpClient(client).getString(request), LibrarySearchResponse.class);
        return dtoConverter.toLibraryTitle(response);
    }

}
