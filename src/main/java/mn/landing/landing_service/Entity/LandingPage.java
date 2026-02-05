package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "landing_pages",
        uniqueConstraints = @UniqueConstraint(name = "uk_page_path", columnNames = {"landing_id", "path"}),
        indexes = @Index(name = "idx_pages_landing", columnList = "landing_id")
)
public class LandingPage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landing_id", nullable = false)
    private Landing landing;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String path; // "/", "/about"

    @Column(nullable = false)
    private Integer orderIndex = 0;
}
