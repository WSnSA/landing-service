package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ComponentService {

    private final LandingSectionRepository sectionRepository;
    private final LandingComponentRepository componentRepository;

    public ComponentService(LandingSectionRepository sectionRepository,
                            LandingComponentRepository componentRepository) {
        this.sectionRepository = sectionRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public ComponentResponse create(User owner, Long sectionId, CreateComponentRequest req) {
        LandingSection section = sectionRepository.findByIdAndPage_Landing_OwnerAndDeletedFalse(sectionId, owner)
                .orElseThrow(() -> new RuntimeException("SECTION_NOT_FOUND"));

        assertDraftEditable(section.getPage().getLanding());

        LandingComponent c = new LandingComponent();
        c.setSection(section);
        c.setComponentType(req.componentType.trim());
        c.setPropsJson(req.propsJson);
        c.setOrderIndex(req.orderIndex == null ? 0 : req.orderIndex);

        return toResponse(componentRepository.save(c));
    }

    public List<ComponentResponse> list(User owner, Long sectionId) {
        return componentRepository.findBySection_IdAndSection_Page_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(sectionId, owner)
                .stream().map(this::toResponse).toList();
    }

    public ComponentResponse get(User owner, Long componentId) {
        LandingComponent c = componentRepository.findByIdAndSection_Page_Landing_OwnerAndDeletedFalse(componentId, owner)
                .orElseThrow(() -> new RuntimeException("COMPONENT_NOT_FOUND"));
        return toResponse(c);
    }

    @Transactional
    public ComponentResponse update(User owner, Long componentId, UpdateComponentRequest req) {
        LandingComponent c = componentRepository.findByIdAndSection_Page_Landing_OwnerAndDeletedFalse(componentId, owner)
                .orElseThrow(() -> new RuntimeException("COMPONENT_NOT_FOUND"));

        assertDraftEditable(c.getSection().getPage().getLanding());

        if (req.componentType != null && !req.componentType.isBlank()) c.setComponentType(req.componentType.trim());
        if (req.propsJson != null) c.setPropsJson(req.propsJson);
        if (req.orderIndex != null) c.setOrderIndex(req.orderIndex);

        return toResponse(componentRepository.save(c));
    }

    @Transactional
    public void delete(User owner, Long componentId) {
        LandingComponent c = componentRepository.findByIdAndSection_Page_Landing_OwnerAndDeletedFalse(componentId, owner)
                .orElseThrow(() -> new RuntimeException("COMPONENT_NOT_FOUND"));

        assertDraftEditable(c.getSection().getPage().getLanding());

        c.setDeleted(true);
        componentRepository.save(c);
    }

    @Transactional
    public void reorder(User owner, Long sectionId, ReorderRequest req) {
        LandingSection section = sectionRepository.findByIdAndPage_Landing_OwnerAndDeletedFalse(sectionId, owner)
                .orElseThrow(() -> new RuntimeException("SECTION_NOT_FOUND"));

        assertDraftEditable(section.getPage().getLanding());

        List<LandingComponent> comps =
                componentRepository.findBySection_IdAndSection_Page_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(sectionId, owner);

        Map<Long, LandingComponent> map = new HashMap<>();
        for (LandingComponent c : comps) map.put(c.getId(), c);

        for (Long id : req.ids) {
            if (!map.containsKey(id)) throw new RuntimeException("ORDER_ID_INVALID");
        }

        int idx = 0;
        for (Long id : req.ids) {
            map.get(id).setOrderIndex(idx++);
        }
        componentRepository.saveAll(map.values());
    }

    private void assertDraftEditable(Landing landing) {
        if (landing.getStatus() == LandingStatus.PUBLISHED) {
            throw new RuntimeException("LANDING_PUBLISHED_READONLY");
        }
    }

    private ComponentResponse toResponse(LandingComponent c) {
        return new ComponentResponse(
                c.getId(),
                c.getSection().getId(),
                c.getComponentType(),
                c.getPropsJson(),
                c.getOrderIndex(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
