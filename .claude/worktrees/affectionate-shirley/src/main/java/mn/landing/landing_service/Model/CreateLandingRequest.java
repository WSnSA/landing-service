package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateLandingRequest {

    @NotBlank(message = "NAME_REQUIRED")
    @Size(min = 2, max = 255, message = "NAME_INVALID")
    public String name;

    // slug-ийг хоосон явуулж болно → name-ээс автоматаар үүсгэнэ
    @Size(max = 120, message = "SLUG_TOO_LONG")
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\s_]*$", message = "SLUG_INVALID_CHARS")
    public String slug;

    public Long templateId;
}
