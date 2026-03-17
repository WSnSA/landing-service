package mn.landing.landing_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TemplateRequest {

    @NotBlank
    @Size(max = 255)
    public String name;

    @NotBlank
    @Size(max = 50)
    public String type; // business, course, personal, product, event, restaurant, portfolio

    @NotBlank
    @Size(max = 500)
    public String description;

    /** Thumbnail зургийн URL — урьдчилж харах зурагт ашиглана */
    public String previewImageUrl;

    /**
     * Template-н бүтэн бүтэц JSON-оор.
     * Бүтэц:
     * {
     *   "pages": [
     *     {
     *       "title": "Нүүр хуудас",
     *       "path": "/",
     *       "orderIndex": 0,
     *       "sections": [
     *         {
     *           "sectionKey": "hero",
     *           "title": "Үндсэн хэсэг",
     *           "orderIndex": 0,
     *           "components": [
     *             {
     *               "componentType": "TEXT",
     *               "orderIndex": 0,
     *               "propsJson": { "text": "Таны компанийн нэр", "fontSize": "48px" }
     *             }
     *           ]
     *         }
     *       ]
     *     }
     *   ]
     * }
     */
    public String schemaJson;
}
