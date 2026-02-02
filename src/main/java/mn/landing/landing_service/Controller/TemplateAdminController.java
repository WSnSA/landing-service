package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Entity.Template;
import mn.landing.landing_service.Repository.TemplateRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/templates")
public class TemplateAdminController {

    private final TemplateRepository repo;

    public TemplateAdminController(TemplateRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Template create(@RequestBody Template t) {
        return repo.save(t);
    }

    @GetMapping
    public List<Template> list() {
        return repo.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

