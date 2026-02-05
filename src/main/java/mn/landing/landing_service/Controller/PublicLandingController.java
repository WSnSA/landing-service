package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.PublicLandingResponse;
import mn.landing.landing_service.Service.PublicLandingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicLandingController {

    private final PublicLandingService publicLandingService;

    public PublicLandingController(PublicLandingService publicLandingService) {
        this.publicLandingService = publicLandingService;
    }

    @GetMapping("/{slug}")
    public PublicLandingResponse bySlug(@PathVariable String slug) {
        return publicLandingService.getPublishedBySlug(slug);
    }
}
