package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_user", columnList = "user_id"),
        @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id")
})
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 120)
    private String action; // LOGIN, CREATE_LANDING, PUBLISH_LANDING...

    @Column(length = 120)
    private String entityType; // "Landing", "Template"...

    private Long entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String metaJson;

    @Column(length = 64)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;
}
