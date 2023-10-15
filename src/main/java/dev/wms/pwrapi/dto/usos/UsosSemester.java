package dev.wms.pwrapi.dto.usos;

import lombok.Builder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Builder
public record UsosSemester(String name, List<UsosCourse> courses, Optional<Set<String>> decisionUrls) {
}
