package mn.landing.landing_service.Service;

import mn.landing.landing_service.Common.PathUtil;
import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PageService {

    private final LandingRepository landingRepository;
    private final LandingPageRepository pageRepository;
    private final LandingSectionRepository sectionRepository;
    private final LandingComponentRepository componentRepository;

    public PageService(LandingRepository landingRepository,
                       LandingPageRepository pageRepository,
                       LandingSectionRepository sectionRepository,
                       LandingComponentRepository componentRepository) {
        this.landingRepository = landingRepository;
        this.pageRepository = pageRepository;
        this.sectionRepository = sectionRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public PageResponse create(User owner, Long landingId, CreatePageRequest req) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(landingId, owner)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        assertDraftEditable(landing);

        String path = PathUtil.normalize(req.path);
        if (path == null || path.isBlank()) throw new RuntimeException("PAGE_PATH_REQUIRED");
        if (pageRepository.existsByLanding_IdAndPathAndDeletedFalse(landingId, path))
            throw new RuntimeException("PAGE_PATH_EXISTS");

        LandingPage p = new LandingPage();
        p.setLanding(landing);
        p.setTitle(req.title);
        p.setPath(path);
        p.setOrderIndex(req.orderIndex == null ? 0 : req.orderIndex);

        LandingPage saved = pageRepository.save(p);
        return toResponse(saved);
    }

    public List<PageResponse> list(User owner, Long landingId) {
        return pageRepository.findByLanding_IdAndLanding_OwnerAndDeletedFalseOrderByOrderIndexAsc(landingId, owner)
                .stream().map(this::toResponse).toList();
    }

    public PageResponse get(User owner, Long pageId) {
        LandingPage page = pageRepository.findByIdAndLanding_OwnerAndDeletedFalse(pageId, owner)
                .orElseThrow(() -> new RuntimeException("PAGE_NOT_FOUND"));
        return toResponse(page);
    }

    @Transactional
    public PageResponse update(User owner, Long pageId, UpdatePageRequest req) {
        LandingPage page = pageRepository.findByIdAndLanding_OwnerAndDeletedFalse(pageId, owner)
                .orElseThrow(() -> new RuntimeException("PAGE_NOT_FOUND"));

        assertDraftEditable(page.getLanding());

        if (req.title != null && !req.title.isBlank()) page.setTitle(req.title);

        if (req.path != null) {
            String newPath = PathUtil.normalize(req.path);
            if (newPath == null || newPath.isBlank()) throw new RuntimeException("PAGE_PATH_REQUIRED");

            Long landingId = page.getLanding().getId();
            if (pageRepository.existsByLanding_IdAndPathAndDeletedFalseAndIdNot(landingId, newPath, page.getId()))
                throw new RuntimeException("PAGE_PATH_EXISTS");

            page.setPath(newPath);
        }

        if (req.orderIndex != null) page.setOrderIndex(req.orderIndex);

        return toResponse(pageRepository.save(page));
    }

    @Transactional
    public void delete(User owner, Long pageId) {
        LandingPage page = pageRepository.findByIdAndLanding_OwnerAndDeletedFalse(pageId, owner)
                .orElseThrow(() -> new RuntimeException("PAGE_NOT_FOUND"));

        assertDraftEditable(page.getLanding());

        componentRepository.softDeleteByPageId(page.getId());
        sectionRepository.softDeleteByPageId(page.getId());

        page.setDeleted(true);
        pageRepository.save(page);
    }

    @Transactional
    public void reorder(User owner, Long landingId, ReorderRequest req) {
        Landing landing = landingRepository.findByIdAndOwnerAndDeletedFalse(landingId, owner)
                .orElseThrow(() -> new RuntimeException("LANDING_NOT_FOUND"));

        assertDraftEditable(landing);

        List<LandingPage> pages =
                pageRepository.findByLanding_IdAndLanding_OwnerAndDeletedFalseOrderByOrderIndexAsc(landingId, owner);

        Map<Long, LandingPage> map = new HashMap<>();
        for (LandingPage p : pages) map.put(p.getId(), p);

        for (Long id : req.ids) {
            if (!map.containsKey(id)) throw new RuntimeException("ORDER_ID_INVALID");
        }

        int idx = 0;
        for (Long id : req.ids) {
            map.get(id).setOrderIndex(idx++);
        }
        pageRepository.saveAll(map.values());
    }

    private void assertDraftEditable(Landing landing) {
        if (landing.getStatus() == LandingStatus.PUBLISHED) {
            throw new RuntimeException("LANDING_PUBLISHED_READONLY");
        }
    }

    private PageResponse toResponse(LandingPage p) {
        return new PageResponse(
                p.getId(),
                p.getLanding().getId(),
                p.getTitle(),
                p.getPath(),
                p.getOrderIndex(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
