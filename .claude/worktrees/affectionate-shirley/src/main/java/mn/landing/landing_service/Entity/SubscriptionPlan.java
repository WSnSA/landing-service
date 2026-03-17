package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans",
        uniqueConstraints = @UniqueConstraint(name = "uk_plans_code", columnNames = "code"))
public class SubscriptionPlan extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String code; // FREE, BUSINESS, PRO

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private BigDecimal priceMonthly = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal priceYearly = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer maxLandings = 1;

    @Column(nullable = false)
    private Integer maxPages = 1;

    @Column(nullable = false)
    private Integer maxStorageMb = 100;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String featuresJson;
}
