package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "landing_components",
        indexes = @Index(name = "idx_components_section", columnList = "section_id")
)
public class LandingComponent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private LandingSection section;

    @Column(name = "component_type", nullable = false, length = 60)
    private String componentType; // "HERO", "TEXT", "IMAGE", "CUSTOM"...

    @Column(name = "props_json", columnDefinition = "json")
    private String propsJson;

    @Column(nullable = false)
    private Integer orderIndex = 0;
}
