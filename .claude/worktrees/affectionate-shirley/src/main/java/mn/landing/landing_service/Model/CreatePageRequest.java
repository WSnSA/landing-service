package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePageRequest {
    @NotBlank(message = "PAGE_TITLE_REQUIRED")
    @Size(max = 255, message = "PAGE_TITLE_TOO_LONG")
    public String title;

    @NotBlank(message = "PAGE_PATH_REQUIRED")
    @Size(max = 255, message = "PAGE_PATH_TOO_LONG")
    public String path;

    public Integer orderIndex;
}
