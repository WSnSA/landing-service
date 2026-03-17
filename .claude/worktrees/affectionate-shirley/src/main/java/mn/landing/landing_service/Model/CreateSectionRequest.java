package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSectionRequest {
    @NotBlank(message = "SECTION_KEY_REQUIRED")
    @Size(max = 120, message = "SECTION_KEY_TOO_LONG")
    public String sectionKey;

    @Size(max = 255, message = "SECTION_TITLE_TOO_LONG")
    public String title;

    public Integer orderIndex;
}
