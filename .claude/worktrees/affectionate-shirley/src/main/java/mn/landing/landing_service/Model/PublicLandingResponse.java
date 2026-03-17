package mn.landing.landing_service.Model;

import java.time.LocalDateTime;
import java.util.List;

public class PublicLandingResponse {

    public Long id;
    public String name;
    public String slug;
    public String seoTitle;
    public String seoDescription;
    public String configJson;
    public LocalDateTime publishedAt;

    public List<PublicPage> pages;

    public PublicLandingResponse(Long id, String name, String slug,
                                 String seoTitle, String seoDescription, String configJson,
                                 LocalDateTime publishedAt,
                                 List<PublicPage> pages) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.configJson = configJson;
        this.publishedAt = publishedAt;
        this.pages = pages;
    }

    public static class PublicPage {
        public Long id;
        public String title;
        public String path;
        public Integer orderIndex;
        public List<PublicSection> sections;

        public PublicPage(Long id, String title, String path, Integer orderIndex, List<PublicSection> sections) {
            this.id = id;
            this.title = title;
            this.path = path;
            this.orderIndex = orderIndex;
            this.sections = sections;
        }
    }

    public static class PublicSection {
        public Long id;
        public String sectionKey;
        public String title;
        public Integer orderIndex;
        public List<PublicComponent> components;

        public PublicSection(Long id, String sectionKey, String title, Integer orderIndex, List<PublicComponent> components) {
            this.id = id;
            this.sectionKey = sectionKey;
            this.title = title;
            this.orderIndex = orderIndex;
            this.components = components;
        }
    }

    public static class PublicComponent {
        public Long id;
        public String componentType;
        public String propsJson;
        public Integer orderIndex;

        public PublicComponent(Long id, String componentType, String propsJson, Integer orderIndex) {
            this.id = id;
            this.componentType = componentType;
            this.propsJson = propsJson;
            this.orderIndex = orderIndex;
        }
    }
}
