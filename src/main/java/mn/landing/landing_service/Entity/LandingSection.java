package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "landing_sections",
        indexes = @Index(name = "idx_sections_page", columnList = "page_id")
)
public class LandingSection extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "page_id", nullable = false)
    private LandingPage page;

    @Column(name = "section_key", nullable = false, length = 120)
    private String sectionKey; // hero, pricing, features...

    @Column(length = 255)
    private String title;

    @Column(nullable = false)
    private Integer orderIndex = 0;
}
