package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Repository.LandingRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionGuard {

    private final SubscriptionService subscriptionService;
    private final LandingRepository landingRepository;

    public SubscriptionGuard(SubscriptionService subscriptionService, LandingRepository landingRepository) {
        this.subscriptionService = subscriptionService;
        this.landingRepository = landingRepository;
    }

    public void assertCanCreateLanding(User user) {
        PlanLimits limits = subscriptionService.getLimits(user);
        long current = landingRepository.countByOwnerAndDeletedFalse(user);
        if (current >= limits.getMaxLandings()) throw new RuntimeException("PLAN_LIMIT_LANDINGS");
    }

    public void assertCanPublish(User user) {
        PlanLimits limits = subscriptionService.getLimits(user);
        if (!limits.isAllowPublish()) throw new RuntimeException("PLAN_LIMIT_PUBLISH");
    }

    // DomainService хийсний дараа ашиглана:
    public void assertCanAddCustomDomain(User user, long currentDomains) {
        PlanLimits limits = subscriptionService.getLimits(user);
        if (!limits.isAllowCustomDomain()) throw new RuntimeException("PLAN_LIMIT_CUSTOM_DOMAIN");
        if (currentDomains >= limits.getMaxDomains()) throw new RuntimeException("PLAN_LIMIT_DOMAINS");
    }
}
