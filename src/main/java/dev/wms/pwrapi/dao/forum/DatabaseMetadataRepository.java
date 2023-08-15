package dev.wms.pwrapi.dao.forum;

import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DatabaseMetadataRepository extends PagingAndSortingRepository<DatabaseMetadataDTO, Long> {

    @Query("SELECT (SELECT COUNT(*) FROM teacher) AS 'total_teachers', (SELECT COUNT(*) FROM review) AS 'total_reviews', " +
            "(SELECT refresh_date FROM refresh_data) AS 'latest_refresh'")
    DatabaseMetadataDTO getDatabaseMetadata();
}