package dev.wms.pwrapi.service.token;

import dev.wms.pwrapi.dao.token.ConfirmationTokenRepository;
import dev.wms.pwrapi.entity.token.ConfirmationToken;
import dev.wms.pwrapi.entity.user.ApiUser;
import dev.wms.pwrapi.utils.generalExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final ConfirmationTokenRepository tokenRepository;

    @Value("${token.confirmation.expiration.hours}")
    private int TOKEN_EXPIRATION_HOURS;

    @Value("${token.confirmation.expiration.minutes}")
    private int TOKEN_EXPIRATION_MINUTES;

    @Override
    public UUID generateUUID() {
        return UUID.randomUUID();
    }

    @Override
    public ConfirmationToken addConfirmationTokenToUser(ApiUser user) {
        ConfirmationToken confirmationToken =
                ConfirmationToken.builder()
                    .expiresAt(
                            LocalDateTime.now()
                                    .plusHours(TOKEN_EXPIRATION_HOURS)
                                    .plusMinutes(TOKEN_EXPIRATION_MINUTES)
                    )
                    .token(generateUUID().toString())
                    .userId(user.getId())
                    .build();

        return tokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("No such token in database"));
    }

    @Override
    public void deleteConfirmationToken(ConfirmationToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteAllConfirmationTokensOfUser(ApiUser user) {
        tokenRepository.deleteAllByUserId(user.getId());
    }
}
