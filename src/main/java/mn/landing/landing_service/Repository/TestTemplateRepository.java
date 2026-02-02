package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.TestTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTemplateRepository
        extends JpaRepository<TestTemplate, Long> {
}
