package dev.wms.pwrapi.service.studentStats.cards.usos;

import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.StudentStatsObject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface UsosCardCreator {

    default List<StudentStatsObject> getAlwaysPresentCards(List<UsosStudies> usosStudies) {
        return Collections.emptyList();
    }

    default List<Optional<StudentStatsObject>> getOptionalCards(List<UsosStudies> usosStudies) {
        return Collections.emptyList();
    }

}
