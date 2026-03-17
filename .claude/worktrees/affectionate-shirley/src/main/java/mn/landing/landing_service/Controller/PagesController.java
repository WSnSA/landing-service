package mn.landing.landing_service.Controller;

import jakarta.validation.Valid;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Security.UserPrincipal;
import mn.landing.landing_service.Service.PageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
public class PagesController {

    private final PageService pageService;

    public PagesController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/{pageId}")
    public PageResponse get(@AuthenticationPrincipal UserPrincipal principal,
                            @PathVariable Long pageId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return pageService.get(principal.getUser(), pageId);
    }

    @PutMapping("/{pageId}")
    public PageResponse update(@AuthenticationPrincipal UserPrincipal principal,
                               @PathVariable Long pageId,
                               @Valid @RequestBody UpdatePageRequest req) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return pageService.update(principal.getUser(), pageId, req);
    }

    @DeleteMapping("/{pageId}")
    public void delete(@AuthenticationPrincipal UserPrincipal principal,
                       @PathVariable Long pageId) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        pageService.delete(principal.getUser(), pageId);
    }
}
