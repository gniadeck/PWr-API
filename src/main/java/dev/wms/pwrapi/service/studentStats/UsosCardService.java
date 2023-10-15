package dev.wms.pwrapi.service.studentStats;


import dev.wms.pwrapi.dao.auth.UsosAuthDao;
import dev.wms.pwrapi.dao.usos.UsosDataDAO;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.StudentStatsObject;
import dev.wms.pwrapi.service.studentStats.cards.usos.UsosCardCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsosCardService implements StudentStatsDataService {

    private final UsosDataDAO usosDataDAO;
    private final List<UsosCardCreator> usosCardCreators;
    private final UsosAuthDao usosAuthDao;

    @Override
    public CompletableFuture<List<StudentStatsObject>> getData(String login, String password, LocaleContext localeContext) {
        log.info("Creating cards for USOS student stats");
        return CompletableFuture.supplyAsync(() -> {
            LocaleContextHolder.setLocaleContext(localeContext);
            List<UsosStudies> studies = usosDataDAO.getStudies(usosAuthDao.login(login, password));
            if(!hasCourses(studies)) return Collections.emptyList();
            return createCards(studies);
        });
    }

    private boolean hasCourses(List<UsosStudies> userStudies) {
        return userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .map(UsosSemester::courses)
                .mapToLong(Collection::size)
                .sum() > 0;
    }

    private List<StudentStatsObject> createCards(List<UsosStudies> userStudies) {
        var parsedOptionalCards = parseOptionalCards(userStudies);
        var alwaysPresentCards = parseAlwaysPresentCards(userStudies);

        return Stream.of(parsedOptionalCards, alwaysPresentCards)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @NotNull
    private List<StudentStatsObject> parseOptionalCards(List<UsosStudies> userStudies) {
        return usosCardCreators.stream()
                .map(creator -> creator.getOptionalCards(userStudies))
                .flatMap(Collection::stream)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @NotNull
    private List<StudentStatsObject> parseAlwaysPresentCards(List<UsosStudies> userStudies) {
        return usosCardCreators.stream()
                .map(creator -> creator.getAlwaysPresentCards(userStudies))
                .flatMap(Collection::stream)
                .toList();
    }
}
