package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.PublicLandingResponse;
import mn.landing.landing_service.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PublicLandingService {

    private final LandingRepository landingRepository;
    private final LandingPageRepository pageRepository;
    private final LandingSectionRepository sectionRepository;
    private final LandingComponentRepository componentRepository;

    public PublicLandingService(LandingRepository landingRepository,
                                LandingPageRepository pageRepository,
                                LandingSectionRepository sectionRepository,
                                LandingComponentRepository componentRepository) {
        this.landingRepository = landingRepository;
        this.pageRepository = pageRepository;
        this.sectionRepository = sectionRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional(readOnly = true)
    public PublicLandingResponse getPublishedBySlug(String slug) {

        Landing landing = landingRepository.findBySlugAndDeletedFalseAndStatus(slug, LandingStatus.PUBLISHED)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        User owner = landing.getOwner();

        List<LandingPage> pages = pageRepository
                .findByLanding_IdAndLanding_OwnerAndDeletedFalseOrderByOrderIndexAsc(landing.getId(), owner);

        List<PublicLandingResponse.PublicPage> pageDtos = pages.stream().map(p -> {
            List<LandingSection> sections = sectionRepository
                    .findByPage_IdAndPage_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(p.getId(), owner);

            List<PublicLandingResponse.PublicSection> sectionDtos = sections.stream().map(s -> {
                List<LandingComponent> comps = componentRepository
                        .findBySection_IdAndSection_Page_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(s.getId(), owner);

                List<PublicLandingResponse.PublicComponent> compDtos = comps.stream().map(c ->
                        new PublicLandingResponse.PublicComponent(
                                c.getId(),
                                c.getComponentType(),
                                c.getPropsJson(),
                                c.getOrderIndex()
                        )
                ).toList();

                return new PublicLandingResponse.PublicSection(
                        s.getId(),
                        s.getSectionKey(),
                        s.getTitle(),
                        s.getOrderIndex(),
                        compDtos
                );
            }).toList();

            return new PublicLandingResponse.PublicPage(
                    p.getId(),
                    p.getTitle(),
                    p.getPath(),
                    p.getOrderIndex(),
                    sectionDtos
            );
        }).toList();

        return new PublicLandingResponse(
                landing.getId(),
                landing.getName(),
                landing.getSlug(),
                landing.getSeoTitle(),
                landing.getSeoDescription(),
                landing.getConfigJson(),
                landing.getPublishedAt(),
                pageDtos
        );
    }
}
