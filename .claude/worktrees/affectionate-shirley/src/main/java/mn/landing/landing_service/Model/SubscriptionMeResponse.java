package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.SubscriptionStatus;

import java.time.LocalDateTime;

public class SubscriptionMeResponse {
    public String planCode;
    public SubscriptionStatus status;
    public LocalDateTime startAt;
    public LocalDateTime endAt;

    public int maxLandings;
    public int maxDomains;
    public boolean allowCustomDomain;
    public boolean allowPublish;

    public SubscriptionMeResponse(String planCode, SubscriptionStatus status,
                                  LocalDateTime startAt, LocalDateTime endAt,
                                  int maxLandings, int maxDomains, boolean allowCustomDomain, boolean allowPublish) {
        this.planCode = planCode;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
        this.maxLandings = maxLandings;
        this.maxDomains = maxDomains;
        this.allowCustomDomain = allowCustomDomain;
        this.allowPublish = allowPublish;
    }
}
