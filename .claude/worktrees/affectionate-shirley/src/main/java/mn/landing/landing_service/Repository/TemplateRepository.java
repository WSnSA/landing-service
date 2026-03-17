package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
