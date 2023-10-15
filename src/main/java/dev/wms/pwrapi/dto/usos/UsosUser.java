package dev.wms.pwrapi.dto.usos;

import java.util.Map;

public record UsosUser(String first_name, String last_name, UsosStudentStatus student_status, String profile_url,
                       String email, String birth_date, Map<String, String> photo_urls, UsosStudentStatus phd_student_status) {
}