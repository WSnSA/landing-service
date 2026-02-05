package mn.landing.landing_service.Service;

import jakarta.transaction.Transactional;
import mn.landing.landing_service.Common.SlugUtil;
import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.CreateLandingRequest;
import mn.landing.landing_service.Model.LandingResponse;
import mn.landing.landing_service.Model.UpdateLandingRequest;
import mn.landing.landing_service.Repository.LandingRepository;
import mn.landing.landing_service.Repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LandingService {

    private final LandingRepository landingRepository;
    private final TemplateRepository templateRepository;

    public LandingService(LandingRepository landingRepository,
                          TemplateRepository templateRepository) {
        this.landingRepository = landingRepository;
        this.templateRepository = templateRepository;
    }

    @Transactional
    public LandingResponse create(User user, CreateLandingRequest req) {

        String slug = SlugUtil.normalize(req.slug);
        if (slug == null || slug.isBlank()) {
            slug = SlugUtil.normalize(req.name);
        }
        if (slug == null || slug.isBlank()) throw new RuntimeException("SLUG_REQUIRED");
        if (slug.length() < 3) throw new RuntimeException("SLUG_TOO_SHORT");
        if (slug.length() > 120) throw new RuntimeException("SLUG_TOO_LONG");
        if (SlugUtil.isReserved(slug)) throw new RuntimeException("SLUG_RESERVED");

        if (landingRepository.existsBySlugAndDeletedFalse(slug)) {
            throw new RuntimeException("LANDING_SLUG_EXISTS");
        }

        Landing landing = new Landing();
        landing.setOwner(user);
        landing.setName(req.name);
        landing.setSlug(slug);
        landing.setStatus(LandingStatus.DRAFT);

        if (req.templateId != null) {
            Template tpl = templateRepository.findById(req.templateId)
                    .orElseThrow(() -> new RuntimeException("TEMPLATE_NOT_FOUND"));
            landing.setTemplate(tpl);
            // landing.setConfigJson(tpl.getSchemaJson()); // хүсвэл initial state
        }

        Landing saved = landingRepository.save(landing);
        return toResponse(saved);
    }

    public List<LandingResponse> myLandings(User user) {
        return landingRepository.findByOwnerAndDeletedFalseOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LandingResponse get(User user, Long id) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(id, user)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));
        return toResponse(landing);
    }

    @Transactional
    public LandingResponse update(User user, Long id, UpdateLandingRequest req) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(id, user)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        // ✅ Publish policy: published үед засварлахгүй
        assertDraftEditable(landing);

        if (req.name != null && !req.name.isBlank()) {
            landing.setName(req.name);
        }

        if (req.slug != null) {
            String newSlug = SlugUtil.normalize(req.slug);
            if (newSlug == null || newSlug.isBlank()) throw new RuntimeException("SLUG_REQUIRED");
            if (newSlug.length() < 3) throw new RuntimeException("SLUG_TOO_SHORT");
            if (newSlug.length() > 120) throw new RuntimeException("SLUG_TOO_LONG");
            if (SlugUtil.isReserved(newSlug)) throw new RuntimeException("SLUG_RESERVED");

            if (landingRepository.existsBySlugAndDeletedFalseAndIdNot(newSlug, landing.getId())) {
                throw new RuntimeException("LANDING_SLUG_EXISTS");
            }
            landing.setSlug(newSlug);
        }

        if (req.seoTitle != null) landing.setSeoTitle(req.seoTitle);
        if (req.seoDescription != null) landing.setSeoDescription(req.seoDescription);
        if (req.configJson != null) landing.setConfigJson(req.configJson);

        Landing saved = landingRepository.save(landing);
        return toResponse(saved);
    }

    @Transactional
    public void delete(User user, Long id) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(id, user)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        landing.setDeleted(true);
        landingRepository.save(landing);
    }

    @Transactional
    public LandingResponse publish(User user, Long id) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(id, user)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        landing.setStatus(LandingStatus.PUBLISHED);
        landing.setPublishedAt(LocalDateTime.now());

        return toResponse(landingRepository.save(landing));
    }

    @Transactional
    public LandingResponse unpublish(User user, Long id) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(id, user)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        landing.setStatus(LandingStatus.DRAFT);
        landing.setPublishedAt(null);

        return toResponse(landingRepository.save(landing));
    }

    private void assertDraftEditable(Landing landing) {
        if (landing.getStatus() == LandingStatus.PUBLISHED) {
            throw new RuntimeException("LANDING_PUBLISHED_READONLY");
        }
    }

    private LandingResponse toResponse(Landing l) {
        Long templateId = (l.getTemplate() == null) ? null : l.getTemplate().getId();
        return new LandingResponse(
                l.getId(),
                l.getName(),
                l.getSlug(),
                l.getStatus(),
                templateId,
                l.getSeoTitle(),
                l.getSeoDescription(),
                l.getCreatedAt(),
                l.getUpdatedAt(),
                l.getPublishedAt()
        );
    }
}
