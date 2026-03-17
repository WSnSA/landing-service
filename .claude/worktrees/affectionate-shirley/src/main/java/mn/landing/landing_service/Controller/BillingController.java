package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Entity.Subscription;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Service.CurrentUserService;
import mn.landing.landing_service.Service.PlanLimits;
import mn.landing.landing_service.Service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final CurrentUserService currentUserService;
    private final SubscriptionService subscriptionService;

    public BillingController(CurrentUserService currentUserService,
                             SubscriptionService subscriptionService) {
        this.currentUserService = currentUserService;
        this.subscriptionService = subscriptionService;
    }

    public record LimitsDto(int maxLandings, int maxDomains, boolean allowCustomDomain, boolean allowPublish) {}
    public record ActiveSubscriptionDto(Long id, String planCode, String status, LocalDateTime startAt, LocalDateTime endAt) {}

    public record BillingCurrentResponse(
            String effectivePlanCode,
            LimitsDto limits,
            ActiveSubscriptionDto activeSubscription
    ) {}

    @GetMapping("/current")
    public BillingCurrentResponse current() {
        User me = currentUserService.requireUser();

        String effectivePlan = subscriptionService.getEffectivePlanCode(me);
        PlanLimits limits = subscriptionService.getLimits(me);

        Subscription active = subscriptionService.getActive(me).orElse(null);

        ActiveSubscriptionDto subDto = null;
        if (active != null) {
            String planCode = (active.getPlan() == null) ? null : active.getPlan().getCode();
            String status = (active.getStatus() == null) ? null : active.getStatus().toString();
            subDto = new ActiveSubscriptionDto(active.getId(), planCode, status, active.getStartAt(), active.getEndAt());
        }

        LimitsDto limitsDto = new LimitsDto(
                limits.getMaxLandings(),
                limits.getMaxDomains(),
                limits.isAllowCustomDomain(),
                limits.isAllowPublish()
        );

        return new BillingCurrentResponse(effectivePlan, limitsDto, subDto);
    }
}
