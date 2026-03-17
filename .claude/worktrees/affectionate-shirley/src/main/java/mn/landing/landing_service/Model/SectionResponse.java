package mn.landing.landing_service.Model;

import java.time.LocalDateTime;

public class SectionResponse {
    public Long id;
    public Long pageId;
    public String sectionKey;
    public String title;
    public Integer orderIndex;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public SectionResponse(Long id, Long pageId, String sectionKey, String title, Integer orderIndex,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.pageId = pageId;
        this.sectionKey = sectionKey;
        this.title = title;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
