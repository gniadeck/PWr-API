package dev.wms.pwrapi.dao.prowadzacy;

import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;

public interface ProwadzacyDAO {
    /**
     * Checks if website is reachable from API host
     * @return OK if connection is accessible (working on DTO for health endpoints)
     */
    String connectToWebsite();

    /**
     * Returns plan for given teacher
     * @param query Teacher name or surname which will be queried on prowadzacy server
     * @param offset Offset in weeks back (+) or forth (-)
     * @return DTO ProwadzacyResult consisting of lessons of given teacher
     */
    ProwadzacyResult getPlanForTeacherQuery(String query, Integer offset);

    /**
     * Returns plan for given room. Please be careful when querying, because due to host architecture,
     * very precise query is needed
     * @param building Building you want to query in, for example D-20
     * @param room Room in the given building
     * @param offset Offset in weeks back (+) or forth (-)
     * @return DTO ProwadzacyResult consisting of lessons that take place in given room
     */
    ProwadzacyResult getPlanForRoomQuery(String building, String room, Integer offset);

    /**
     * Returns plan of given subject. Please be aware, that this endpoint needs really precise query and won't
     * work on any spelling mistakes (for ex. Algorytmy i struktury dany) wont work due to mising "ch"
     * @param query Name of the subject
     * @param offset Offset in weeks back (+) or forth (-)
     * @return DTO ProwadzacyResult consisting of lessons in given subject (few can take place at one time)
     */
    ProwadzacyResult getPlanForSubjectQuery(String query, Integer offset);
}
