package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "domains",
        uniqueConstraints = @UniqueConstraint(name = "uk_domains_host", columnNames = "host"),
        indexes = @Index(name = "idx_domains_landing", columnList = "landing_id")
)
public class Domain extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landing_id", nullable = false)
    private Landing landing;

    @Column(nullable = false, length = 255)
    private String host; // example.com

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DomainType domainType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DomainStatus status = DomainStatus.PENDING;

    private String verificationToken;

    private LocalDateTime sslIssuedAt;
}
