package dev.wms.pwrapi.dao.user;

import dev.wms.pwrapi.entity.user.ApiUser;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ApiUserRepository extends CrudRepository<ApiUser, Long> {

    Optional<ApiUser> getApiUserByApiKey(String apiKey);

    Optional<ApiUser> getApiUserByEmail(String email);

    @Modifying
    @Query("UPDATE api_user user " +
           "SET user.requests_left = :requestsLeft, user.requests_left_updated_at = :lastRequestTimestamp " +
           "WHERE user.id = :userId")
    void updateRequestDataById(Long userId, Integer requestsLeft, LocalDateTime lastRequestTimestamp);
}
