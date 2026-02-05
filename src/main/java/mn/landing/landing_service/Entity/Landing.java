package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "landings", indexes = {
        @Index(name = "idx_landings_user", columnList = "user_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_landings_slug", columnNames = "slug")
})
public class Landing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug; // default subdomain slug

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LandingStatus status = LandingStatus.DRAFT;

    private String seoTitle;

    @Column(length = 500)
    private String seoDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String configJson; // builder state / overrides

    private LocalDateTime publishedAt;
}
