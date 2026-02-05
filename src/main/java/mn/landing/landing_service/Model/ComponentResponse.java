package mn.landing.landing_service.Model;

import java.time.LocalDateTime;

public class ComponentResponse {
    public Long id;
    public Long sectionId;
    public String componentType;
    public String propsJson;
    public Integer orderIndex;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ComponentResponse(Long id, Long sectionId, String componentType, String propsJson, Integer orderIndex,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sectionId = sectionId;
        this.componentType = componentType;
        this.propsJson = propsJson;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
