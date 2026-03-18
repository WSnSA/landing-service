package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Getter @Setter
public class Plan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // FREE | PRO | BUSINESS

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "monthly_price", nullable = false)
    private Integer monthlyPrice = 0;

    @Column(name = "max_landings", nullable = false)
    private Integer maxLandings = 1;

    @Column(name = "max_domains", nullable = false)
    private Integer maxDomains = 0;

    @Column(name = "allow_publish", nullable = false)
    private Boolean allowPublish = true;

    @Column(name = "allow_custom_domain", nullable = false)
    private Boolean allowCustomDomain = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
