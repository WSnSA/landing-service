package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "form_submissions", indexes = {
        @Index(name = "idx_submissions_landing", columnList = "landing_id")
})
public class FormSubmission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landing_id", nullable = false)
    private Landing landing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private LandingPage page;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private String dataJson;

    @Column(length = 64)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;
}
