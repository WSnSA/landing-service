package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_sub_user", columnList = "user_id")
})
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime renewalAt;

    private String providerCustomerId;
}
