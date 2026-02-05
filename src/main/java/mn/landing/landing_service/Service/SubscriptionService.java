package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.Subscription;
import mn.landing.landing_service.Entity.SubscriptionStatus;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanCatalog planCatalog;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PlanCatalog planCatalog) {
        this.subscriptionRepository = subscriptionRepository;
        this.planCatalog = planCatalog;
    }

    public Optional<Subscription> getActive(User user) {
        return subscriptionRepository
                .findFirstByUserAndDeletedFalseAndStatusAndEndAtAfterOrderByEndAtDesc(
                        user, SubscriptionStatus.ACTIVE, LocalDateTime.now()
                );
    }

    public String getEffectivePlanCode(User user) {
        return getActive(user)
                .map(sub -> sub.getPlan() != null ? sub.getPlan().getCode() : "FREE")
                .orElse("FREE");
    }

    public PlanLimits getLimits(User user) {
        return planCatalog.limitsFor(getEffectivePlanCode(user));
    }
}
