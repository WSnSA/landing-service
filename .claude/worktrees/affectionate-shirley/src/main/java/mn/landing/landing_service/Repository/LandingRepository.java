package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.Landing;
import mn.landing.landing_service.Entity.LandingStatus;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LandingRepository extends JpaRepository<Landing, Long> {

    List<Landing> findByOwnerAndDeletedFalseOrderByCreatedAtDesc(User owner);

    Optional<Landing> findByIdAndOwnerAndDeletedFalse(Long id, User owner);

    boolean existsBySlugAndDeletedFalse(String slug);

    boolean existsBySlugAndDeletedFalseAndIdNot(String slug, Long id);

    long countByOwnerAndDeletedFalse(User user);

    // ✅ Public API ашиглана (зөвхөн published)
    Optional<Landing> findBySlugAndDeletedFalseAndStatus(String slug, LandingStatus status);
}
