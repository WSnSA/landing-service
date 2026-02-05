package mn.landing.landing_service.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlanCatalog {

    // FREE
    @Value("${app.plans.FREE.maxLandings:1}") private int freeMaxLandings;
    @Value("${app.plans.FREE.maxDomains:0}") private int freeMaxDomains;
    @Value("${app.plans.FREE.allowCustomDomain:false}") private boolean freeAllowCustomDomain;
    @Value("${app.plans.FREE.allowPublish:true}") private boolean freeAllowPublish;

    // PRO
    @Value("${app.plans.PRO.maxLandings:5}") private int proMaxLandings;
    @Value("${app.plans.PRO.maxDomains:3}") private int proMaxDomains;
    @Value("${app.plans.PRO.allowCustomDomain:true}") private boolean proAllowCustomDomain;
    @Value("${app.plans.PRO.allowPublish:true}") private boolean proAllowPublish;

    // BUSINESS
    @Value("${app.plans.BUSINESS.maxLandings:9999}") private int bizMaxLandings;
    @Value("${app.plans.BUSINESS.maxDomains:50}") private int bizMaxDomains;
    @Value("${app.plans.BUSINESS.allowCustomDomain:true}") private boolean bizAllowCustomDomain;
    @Value("${app.plans.BUSINESS.allowPublish:true}") private boolean bizAllowPublish;

    public PlanLimits limitsFor(String planCode) {
        String code = (planCode == null || planCode.isBlank()) ? "FREE" : planCode.trim().toUpperCase();

        return switch (code) {
            case "PRO" -> new PlanLimits(proMaxLandings, proMaxDomains, proAllowCustomDomain, proAllowPublish);
            case "BUSINESS" -> new PlanLimits(bizMaxLandings, bizMaxDomains, bizAllowCustomDomain, bizAllowPublish);
            default -> new PlanLimits(freeMaxLandings, freeMaxDomains, freeAllowCustomDomain, freeAllowPublish);
        };
    }
}
