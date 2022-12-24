package dev.wms.pwrapi.repository;

import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DatabaseMetadataRepository extends PagingAndSortingRepository<DatabaseMetadataDTO_r, Long> {

    @Query("SELECT (SELECT COUNT(*) FROM teacher), (SELECT COUNT(*) FROM review), (SELECT refresh_date FROM refresh_data)")
    DatabaseMetadataDTO_r getDatabaseMetadata();
}