package dev.wms.pwrapi.service.token;

import dev.wms.pwrapi.entity.token.ConfirmationToken;
import dev.wms.pwrapi.entity.user.ApiUser;

import java.util.UUID;

public interface TokenService {

    UUID generateUUID();

    ConfirmationToken addConfirmationTokenToUser(ApiUser user);

    ConfirmationToken getConfirmationToken(String token);

    void deleteConfirmationToken(ConfirmationToken token);

    void deleteAllConfirmationTokensOfUser(ApiUser user);
}
