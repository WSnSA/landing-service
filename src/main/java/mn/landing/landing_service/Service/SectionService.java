package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SectionService {

    private final LandingPageRepository pageRepository;
    private final LandingSectionRepository sectionRepository;
    private final LandingComponentRepository componentRepository;

    public SectionService(LandingPageRepository pageRepository,
                          LandingSectionRepository sectionRepository,
                          LandingComponentRepository componentRepository) {
        this.pageRepository = pageRepository;
        this.sectionRepository = sectionRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public SectionResponse create(User owner, Long pageId, CreateSectionRequest req) {
        LandingPage page = pageRepository.findByIdAndLanding_OwnerAndDeletedFalse(pageId, owner)
                .orElseThrow(() -> new RuntimeException("PAGE_NOT_FOUND"));

        assertDraftEditable(page.getLanding());

        LandingSection s = new LandingSection();
        s.setPage(page);
        s.setSectionKey(req.sectionKey.trim());
        s.setTitle(req.title);
        s.setOrderIndex(req.orderIndex == null ? 0 : req.orderIndex);

        return toResponse(sectionRepository.save(s));
    }

    public List<SectionResponse> list(User owner, Long pageId) {
        return sectionRepository.findByPage_IdAndPage_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(pageId, owner)
                .stream().map(this::toResponse).toList();
    }

    public SectionResponse get(User owner, Long sectionId) {
        LandingSection s = sectionRepository.findByIdAndPage_Landing_OwnerAndDeletedFalse(sectionId, owner)
                .orElseThrow(() -> new RuntimeException("SECTION_NOT_FOUND"));
        return toResponse(s);
    }

    @Transactional
    public SectionResponse update(User owner, Long sectionId, UpdateSectionRequest req) {
        LandingSection s = sectionRepository.findByIdAndPage_Landing_OwnerAndDeletedFalse(sectionId, owner)
                .orElseThrow(() -> new RuntimeException("SECTION_NOT_FOUND"));

        assertDraftEditable(s.getPage().getLanding());

        if (req.sectionKey != null && !req.sectionKey.isBlank()) s.setSectionKey(req.sectionKey.trim());
        if (req.title != null) s.setTitle(req.title);
        if (req.orderIndex != null) s.setOrderIndex(req.orderIndex);

        return toResponse(sectionRepository.save(s));
    }

    @Transactional
    public void delete(User owner, Long sectionId) {
        LandingSection s = sectionRepository.findByIdAndPage_Landing_OwnerAndDeletedFalse(sectionId, owner)
                .orElseThrow(() -> new RuntimeException("SECTION_NOT_FOUND"));

        assertDraftEditable(s.getPage().getLanding());

        componentRepository.softDeleteBySectionId(s.getId());

        s.setDeleted(true);
        sectionRepository.save(s);
    }

    @Transactional
    public void reorder(User owner, Long pageId, ReorderRequest req) {
        LandingPage page = pageRepository.findByIdAndLanding_OwnerAndDeletedFalse(pageId, owner)
                .orElseThrow(() -> new RuntimeException("PAGE_NOT_FOUND"));

        assertDraftEditable(page.getLanding());

        List<LandingSection> sections =
                sectionRepository.findByPage_IdAndPage_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(pageId, owner);

        Map<Long, LandingSection> map = new HashMap<>();
        for (LandingSection s : sections) map.put(s.getId(), s);

        for (Long id : req.ids) {
            if (!map.containsKey(id)) throw new RuntimeException("ORDER_ID_INVALID");
        }

        int idx = 0;
        for (Long id : req.ids) {
            map.get(id).setOrderIndex(idx++);
        }
        sectionRepository.saveAll(map.values());
    }

    private void assertDraftEditable(Landing landing) {
        if (landing.getStatus() == LandingStatus.PUBLISHED) {
            throw new RuntimeException("LANDING_PUBLISHED_READONLY");
        }
    }

    private SectionResponse toResponse(LandingSection s) {
        return new SectionResponse(
                s.getId(),
                s.getPage().getId(),
                s.getSectionKey(),
                s.getTitle(),
                s.getOrderIndex(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }
}
