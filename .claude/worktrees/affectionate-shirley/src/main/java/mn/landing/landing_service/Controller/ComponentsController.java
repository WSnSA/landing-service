package mn.landing.landing_service.Controller;

import jakarta.validation.Valid;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Security.UserPrincipal;
import mn.landing.landing_service.Service.ComponentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ComponentsController {

    private final ComponentService componentService;

    public ComponentsController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping("/api/sections/{sectionId}/components")
    public ComponentResponse create(@AuthenticationPrincipal UserPrincipal principal,
                                    @PathVariable Long sectionId,
                                    @Valid @RequestBody CreateComponentRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return componentService.create(principal.getUser(), sectionId, req);
    }

    @GetMapping("/api/sections/{sectionId}/components")
    public List<ComponentResponse> list(@AuthenticationPrincipal UserPrincipal principal,
                                        @PathVariable Long sectionId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return componentService.list(principal.getUser(), sectionId);
    }

    @PostMapping("/api/sections/{sectionId}/components/reorder")
    public void reorder(@AuthenticationPrincipal UserPrincipal principal,
                        @PathVariable Long sectionId,
                        @Valid @RequestBody ReorderRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        componentService.reorder(principal.getUser(), sectionId, req);
    }

    @GetMapping("/api/components/{componentId}")
    public ComponentResponse get(@AuthenticationPrincipal UserPrincipal principal,
                                 @PathVariable Long componentId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return componentService.get(principal.getUser(), componentId);
    }

    @PutMapping("/api/components/{componentId}")
    public ComponentResponse update(@AuthenticationPrincipal UserPrincipal principal,
                                    @PathVariable Long componentId,
                                    @Valid @RequestBody UpdateComponentRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return componentService.update(principal.getUser(), componentId, req);
    }

    @DeleteMapping("/api/components/{componentId}")
    public void delete(@AuthenticationPrincipal UserPrincipal principal,
                       @PathVariable Long componentId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        componentService.delete(principal.getUser(), componentId);
    }
}
