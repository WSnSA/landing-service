package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.LandingPage;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LandingPageRepository extends JpaRepository<LandingPage, Long> {

    List<LandingPage> findByLanding_IdAndLanding_OwnerAndDeletedFalseOrderByOrderIndexAsc(Long landingId, User owner);

    Optional<LandingPage> findByIdAndLanding_OwnerAndDeletedFalse(Long id, User owner);

    boolean existsByLanding_IdAndPathAndDeletedFalse(Long landingId, String path);

    boolean existsByLanding_IdAndPathAndDeletedFalseAndIdNot(Long landingId, String path, Long id);
}
