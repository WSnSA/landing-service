package mn.landing.landing_service.Controller;

import jakarta.validation.Valid;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Security.UserPrincipal;
import mn.landing.landing_service.Service.PageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landings/{landingId}/pages")
public class LandingPagesController {

    private final PageService pageService;

    public LandingPagesController(PageService pageService) {
        this.pageService = pageService;
    }

    @PostMapping
    public PageResponse create(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long landingId,
                               @Valid @RequestBody CreatePageRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return pageService.create(principal.getUser(), landingId, req);
    }

    @GetMapping
    public List<PageResponse> list(@AuthenticationPrincipal UserPrincipal principal,
                                   @PathVariable Long landingId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return pageService.list(principal.getUser(), landingId);
    }

    @PostMapping("/reorder")
    public void reorder(@AuthenticationPrincipal UserPrincipal principal,
                        @PathVariable Long landingId,
                        @Valid @RequestBody ReorderRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        pageService.reorder(principal.getUser(), landingId, req);
    }
}
