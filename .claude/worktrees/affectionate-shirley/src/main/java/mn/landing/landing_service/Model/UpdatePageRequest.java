package mn.landing.landing_service.Model;

import jakarta.validation.constraints.Size;

public class UpdatePageRequest {
    @Size(max = 255, message = "PAGE_TITLE_TOO_LONG")
    public String title;

    @Size(max = 255, message = "PAGE_PATH_TOO_LONG")
    public String path;

    public Integer orderIndex;
}
