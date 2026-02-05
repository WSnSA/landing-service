package mn.landing.landing_service.Controller;

import jakarta.validation.Valid;
import mn.landing.landing_service.Model.CreateLandingRequest;
import mn.landing.landing_service.Model.LandingResponse;
import mn.landing.landing_service.Model.UpdateLandingRequest;
import mn.landing.landing_service.Security.UserPrincipal;
import mn.landing.landing_service.Service.LandingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landings")
public class LandingController {

    private final LandingService landingService;

    public LandingController(LandingService landingService) {
        this.landingService = landingService;
    }

    @PostMapping
    public LandingResponse create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateLandingRequest req
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.create(principal.getUser(), req);
    }

    @GetMapping
    public List<LandingResponse> myLandings(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.myLandings(principal.getUser());
    }

    @GetMapping("/{id}")
    public LandingResponse get(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.get(principal.getUser(), id);
    }

    @PutMapping("/{id}")
    public LandingResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateLandingRequest req
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.update(principal.getUser(), id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        landingService.delete(principal.getUser(), id);
    }

    @PostMapping("/{id}/publish")
    public LandingResponse publish(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.publish(principal.getUser(), id);
    }

    @PostMapping("/{id}/unpublish")
    public LandingResponse unpublish(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        if (principal == null) throw new RuntimeException("UNAUTHORIZED");
        return landingService.unpublish(principal.getUser(), id);
    }
}
