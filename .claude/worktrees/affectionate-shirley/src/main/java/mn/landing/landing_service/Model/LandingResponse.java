package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.LandingStatus;

import java.time.LocalDateTime;

public class LandingResponse {
    public Long id;
    public String name;
    public String slug;
    public LandingStatus status;

    public Long templateId;

    public String seoTitle;
    public String seoDescription;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public LocalDateTime publishedAt;

    public LandingResponse(Long id, String name, String slug, LandingStatus status,
                           Long templateId, String seoTitle, String seoDescription,
                           LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.status = status;
        this.templateId = templateId;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
    }
}
