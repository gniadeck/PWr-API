package dev.wms.pwrapi.dao.token;

import dev.wms.pwrapi.entity.token.ConfirmationToken;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
        Optional<ConfirmationToken> findByToken(String token);

        @Modifying
        @Query("DELETE FROM opinie.confirmation_token WHERE user_id = :userId")
        void deleteAllByUserId(Long userId);
}
