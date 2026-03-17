package mn.landing.landing_service.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.TemplateRequest;
import mn.landing.landing_service.Model.TemplateResponse;
import mn.landing.landing_service.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final LandingPageRepository pageRepository;
    private final LandingSectionRepository sectionRepository;
    private final LandingComponentRepository componentRepository;
    private final ObjectMapper objectMapper;

    public TemplateService(TemplateRepository templateRepository,
                           LandingPageRepository pageRepository,
                           LandingSectionRepository sectionRepository,
                           LandingComponentRepository componentRepository,
                           ObjectMapper objectMapper) {
        this.templateRepository = templateRepository;
        this.pageRepository = pageRepository;
        this.sectionRepository = sectionRepository;
        this.componentRepository = componentRepository;
        this.objectMapper = objectMapper;
    }

    // ── Admin CRUD ──────────────────────────────────────────────────────────────

    @Transactional
    public TemplateResponse create(TemplateRequest req) {
        Template t = new Template();
        t.setName(req.name);
        t.setType(req.type);
        t.setDescription(req.description);
        t.setPreviewImageUrl(req.previewImageUrl);
        t.setSchemaJson(req.schemaJson);
        return toResponse(templateRepository.save(t));
    }

    @Transactional
    public TemplateResponse update(Long id, TemplateRequest req) {
        Template t = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TEMPLATE_NOT_FOUND"));
        t.setName(req.name);
        t.setType(req.type);
        t.setDescription(req.description);
        t.setPreviewImageUrl(req.previewImageUrl);
        t.setSchemaJson(req.schemaJson);
        return toResponse(templateRepository.save(t));
    }

    @Transactional
    public void delete(Long id) {
        if (!templateRepository.existsById(id)) throw new RuntimeException("TEMPLATE_NOT_FOUND");
        templateRepository.deleteById(id);
    }

    public List<TemplateResponse> listAll() {
        return templateRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TemplateResponse getById(Long id) {
        return templateRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("TEMPLATE_NOT_FOUND"));
    }

    // ── Template → Landing руу хуулах ───────────────────────────────────────────

    /**
     * Template-н schemaJson-г Landing-д хуулж Page/Section/Component үүсгэнэ.
     * Аль хэдийн page байвал дахин үүсгэхгүй (idempotent биш — зөвхөн шинэ landing-д ашиглана).
     */
    @Transactional
    public void applyTemplateToLanding(Template template, Landing landing) {
        String schema = template.getSchemaJson();
        if (schema == null || schema.isBlank()) return;

        try {
            Map<String, Object> root = objectMapper.readValue(schema, new TypeReference<>() {});
            List<Map<String, Object>> pages = castList(root.get("pages"));
            if (pages == null) return;

            for (Map<String, Object> pageMap : pages) {
                LandingPage page = new LandingPage();
                page.setLanding(landing);
                page.setTitle(strVal(pageMap, "title", "Хуудас"));
                page.setPath(strVal(pageMap, "path", "/"));
                page.setOrderIndex(intVal(pageMap, "orderIndex", 0));
                page = pageRepository.save(page);

                List<Map<String, Object>> sections = castList(pageMap.get("sections"));
                if (sections == null) continue;

                for (Map<String, Object> secMap : sections) {
                    LandingSection section = new LandingSection();
                    section.setPage(page);
                    section.setSectionKey(strVal(secMap, "sectionKey", "hero"));
                    section.setTitle(strVal(secMap, "title", null));
                    section.setOrderIndex(intVal(secMap, "orderIndex", 0));
                    section = sectionRepository.save(section);

                    List<Map<String, Object>> components = castList(secMap.get("components"));
                    if (components == null) continue;

                    for (Map<String, Object> cmpMap : components) {
                        LandingComponent component = new LandingComponent();
                        component.setSection(section);
                        component.setComponentType(strVal(cmpMap, "componentType", "TEXT"));
                        component.setOrderIndex(intVal(cmpMap, "orderIndex", 0));

                        // propsJson нь object эсвэл string байж болно
                        Object propsRaw = cmpMap.get("propsJson");
                        if (propsRaw instanceof String) {
                            component.setPropsJson((String) propsRaw);
                        } else if (propsRaw != null) {
                            component.setPropsJson(objectMapper.writeValueAsString(propsRaw));
                        }
                        componentRepository.save(component);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("TEMPLATE_SCHEMA_INVALID: " + e.getMessage());
        }
    }

    // ── Helper ──────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(Object obj) {
        if (obj instanceof List<?> list) {
            return (List<Map<String, Object>>) list;
        }
        return null;
    }

    private String strVal(Map<String, Object> map, String key, String defaultVal) {
        Object v = map.get(key);
        return (v instanceof String s && !s.isBlank()) ? s : defaultVal;
    }

    private int intVal(Map<String, Object> map, String key, int defaultVal) {
        Object v = map.get(key);
        if (v instanceof Number n) return n.intValue();
        return defaultVal;
    }

    private TemplateResponse toResponse(Template t) {
        return new TemplateResponse(
                t.getId(),
                t.getName(),
                t.getType(),
                t.getDescription(),
                t.getPreviewImageUrl(),
                t.getSchemaJson(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
