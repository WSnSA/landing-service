package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "templates", indexes = {
        @Index(name = "idx_templates_type", columnList = "type")
})
public class Template extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 50)
    private String type; // business, wedding, etc

    @Column(nullable = false, length = 500)
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String schemaJson;
}
