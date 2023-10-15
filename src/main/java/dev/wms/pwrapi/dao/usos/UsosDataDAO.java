package dev.wms.pwrapi.dao.usos;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.dto.usos.UsosUser;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class UsosDataDAO {

    private final UsosApiClient apiClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public UsosUser getUsosUser(HttpClient client) {
        var response = apiClient.perform(client, "services/users/user", Map.of("fields",
                "id|first_name|last_name|sex|titles|student_status|staff_status|email|profile_url|" +
                        "phone_numbers|mobile_numbers|office_hours|interests|has_photo|photo_urls|student_number|" +
                        "pesel|birth_date|revenue_office_id|citizenship|room|student_programmes|" +
                        "employment_functions|employment_positions|postal_addresses|alt_email|external_ids|phd_student_status"));

        return objectMapper.readValue(response, UsosUser.class);
    }

    public List<UsosStudies> getStudies(HttpClient client) {
        return new UsosStudiesDao(client).parseStudies();
    }

    public Document getMyUsosWebPage(HttpClient client) {
        return client.getDocument("https://web.usos.pwr.edu.pl/kontroler.php?_action=home/index");
    }
}
