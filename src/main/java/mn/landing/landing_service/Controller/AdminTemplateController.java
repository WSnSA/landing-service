package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.TemplateRequest;
import mn.landing.landing_service.Model.TemplateResponse;
import mn.landing.landing_service.Service.TemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/templates")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
public class AdminTemplateController {

    private final TemplateService templateService;

    public AdminTemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public List<TemplateResponse> list() {
        return templateService.listAll();
    }

    @GetMapping("/{id}")
    public TemplateResponse get(@PathVariable Long id) {
        return templateService.getById(id);
    }

    @PostMapping
    public TemplateResponse create(@RequestBody TemplateRequest req) {
        return templateService.create(req);
    }

    @PutMapping("/{id}")
    public TemplateResponse update(@PathVariable Long id, @RequestBody TemplateRequest req) {
        return templateService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        templateService.delete(id);
    }
}
