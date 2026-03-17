package mn.landing.landing_service.Model;

import java.time.LocalDateTime;

public class TemplateResponse {

    public Long id;
    public String name;
    public String type;
    public String description;
    public String previewImageUrl;
    public String schemaJson;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public TemplateResponse(Long id, String name, String type, String description,
                            String previewImageUrl, String schemaJson,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.previewImageUrl = previewImageUrl;
        this.schemaJson = schemaJson;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
