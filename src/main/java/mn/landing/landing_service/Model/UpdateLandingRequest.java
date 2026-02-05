package mn.landing.landing_service.Model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateLandingRequest {

    @Size(min = 2, max = 255, message = "NAME_INVALID")
    public String name;

    @Size(max = 120, message = "SLUG_TOO_LONG")
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\s_]*$", message = "SLUG_INVALID_CHARS")
    public String slug;

    @Size(max = 255, message = "SEO_TITLE_TOO_LONG")
    public String seoTitle;

    @Size(max = 500, message = "SEO_DESC_TOO_LONG")
    public String seoDescription;

    // Builder config state (json string)
    public String configJson;
}
