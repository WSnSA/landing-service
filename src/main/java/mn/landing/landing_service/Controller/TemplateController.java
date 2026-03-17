package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.TemplateResponse;
import mn.landing.landing_service.Service.TemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Бүртгэлтэй хэрэглэгч template-ийн жагсаалтыг харах.
 * schemaJson-г буцаахгүй — зөвхөн нэр, тайлбар, preview.
 */
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public List<TemplateResponse> list() {
        return templateService.listAll();
    }
}
