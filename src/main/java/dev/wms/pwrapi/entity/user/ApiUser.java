package dev.wms.pwrapi.entity.user;

import dev.wms.pwrapi.entity.user.rateLimit.AdjustableRateLimitData;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

@Data
@Builder
public class ApiUser implements UserDetails {

    @Id
    private Long id;
    private String email;
    private String apiKey;
    private boolean enabled;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private AdjustableRateLimitData rateLimitData;

    public Optional<String> getApiKey(){
        return Optional.ofNullable(apiKey);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return apiKey;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
