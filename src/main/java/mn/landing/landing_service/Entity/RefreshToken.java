package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens",
        indexes = {@Index(name = "idx_rt_user", columnList = "user_id")},
        uniqueConstraints = {@UniqueConstraint(name = "uk_refresh_token_hash", columnNames = "token_hash")}
)
public class RefreshToken extends BaseEntity {

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;
}
