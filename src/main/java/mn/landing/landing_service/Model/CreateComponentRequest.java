package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateComponentRequest {
    @NotBlank(message = "COMPONENT_TYPE_REQUIRED")
    @Size(max = 60, message = "COMPONENT_TYPE_TOO_LONG")
    public String componentType;

    public String propsJson;

    public Integer orderIndex;
}
