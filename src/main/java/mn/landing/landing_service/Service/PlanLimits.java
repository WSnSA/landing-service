package mn.landing.landing_service.Service;

public class PlanLimits {
    private final int maxLandings;
    private final int maxDomains;
    private final boolean allowCustomDomain;
    private final boolean allowPublish;

    public PlanLimits(int maxLandings, int maxDomains, boolean allowCustomDomain, boolean allowPublish) {
        this.maxLandings = maxLandings;
        this.maxDomains = maxDomains;
        this.allowCustomDomain = allowCustomDomain;
        this.allowPublish = allowPublish;
    }

    public int getMaxLandings() { return maxLandings; }
    public int getMaxDomains() { return maxDomains; }
    public boolean isAllowCustomDomain() { return allowCustomDomain; }
    public boolean isAllowPublish() { return allowPublish; }
}
