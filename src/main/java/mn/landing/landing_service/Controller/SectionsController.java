package mn.landing.landing_service.Controller;

import jakarta.validation.Valid;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Security.UserPrincipal;
import mn.landing.landing_service.Service.SectionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SectionsController {

    private final SectionService sectionService;

    public SectionsController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/api/pages/{pageId}/sections")
    public SectionResponse create(@AuthenticationPrincipal UserPrincipal principal,
                                  @PathVariable Long pageId,
                                  @Valid @RequestBody CreateSectionRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return sectionService.create(principal.getUser(), pageId, req);
    }

    @GetMapping("/api/pages/{pageId}/sections")
    public List<SectionResponse> list(@AuthenticationPrincipal UserPrincipal principal,
                                      @PathVariable Long pageId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return sectionService.list(principal.getUser(), pageId);
    }

    @PostMapping("/api/pages/{pageId}/sections/reorder")
    public void reorder(@AuthenticationPrincipal UserPrincipal principal,
                        @PathVariable Long pageId,
                        @Valid @RequestBody ReorderRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        sectionService.reorder(principal.getUser(), pageId, req);
    }

    @GetMapping("/api/sections/{sectionId}")
    public SectionResponse get(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long sectionId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return sectionService.get(principal.getUser(), sectionId);
    }

    @PutMapping("/api/sections/{sectionId}")
    public SectionResponse update(@AuthenticationPrincipal UserPrincipal principal,
                                  @PathVariable Long sectionId,
                                  @Valid @RequestBody UpdateSectionRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return sectionService.update(principal.getUser(), sectionId, req);
    }

    @DeleteMapping("/api/sections/{sectionId}")
    public void delete(@AuthenticationPrincipal UserPrincipal principal,
                       @PathVariable Long sectionId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        sectionService.delete(principal.getUser(), sectionId);
    }
}
