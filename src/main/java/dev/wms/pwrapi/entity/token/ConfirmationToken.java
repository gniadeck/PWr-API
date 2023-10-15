package dev.wms.pwrapi.entity.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    private Long id;
    private String token;
    private LocalDateTime expiresAt;
    private Long userId;


    public boolean isExpired(){
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
