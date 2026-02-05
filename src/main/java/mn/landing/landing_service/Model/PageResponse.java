package mn.landing.landing_service.Model;

import java.time.LocalDateTime;

public class PageResponse {
    public Long id;
    public Long landingId;
    public String title;
    public String path;
    public Integer orderIndex;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public PageResponse(Long id, Long landingId, String title, String path, Integer orderIndex,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.landingId = landingId;
        this.title = title;
        this.path = path;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
