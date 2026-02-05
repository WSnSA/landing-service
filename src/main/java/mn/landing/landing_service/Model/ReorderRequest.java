package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ReorderRequest {
    @NotEmpty(message = "ORDER_IDS_REQUIRED")
    public List<Long> ids;
}
