package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Entity.Subscription;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Model.SubscriptionMeResponse;
import mn.landing.landing_service.Service.CurrentUserService;
import mn.landing.landing_service.Service.PlanLimits;
import mn.landing.landing_service.Service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final CurrentUserService currentUserService;
    private final SubscriptionService subscriptionService;

    public SubscriptionController(CurrentUserService currentUserService,
                                  SubscriptionService subscriptionService) {
        this.currentUserService = currentUserService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/me")
    public SubscriptionMeResponse me() {
        User me = currentUserService.requireUser();

        String planCode = subscriptionService.getEffectivePlanCode(me);
        PlanLimits limits = subscriptionService.getLimits(me);

        Subscription sub = subscriptionService.getActive(me).orElse(null);

        return new SubscriptionMeResponse(
                planCode,
                sub == null ? null : sub.getStatus(),
                sub == null ? null : sub.getStartAt(),
                sub == null ? null : sub.getEndAt(),
                limits.getMaxLandings(),
                limits.getMaxDomains(),
                limits.isAllowCustomDomain(),
                limits.isAllowPublish()
        );
    }
}
