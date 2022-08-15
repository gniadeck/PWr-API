package dev.wms.pwrapi.entity.prowadzacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProwadzacyDay {

    private String date;
    private List<ProwadzacyLesson> lessons;

}
