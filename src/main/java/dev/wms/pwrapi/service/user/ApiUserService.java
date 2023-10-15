package dev.wms.pwrapi.service.user;

import dev.wms.pwrapi.entity.user.ApiUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface ApiUserService extends UserDetailsService {

    Optional<ApiUser> getUserByApiKey(String apiKey);

    ApiUser registerUser(String email);

    ApiUser confirmEmail(String token);
}
