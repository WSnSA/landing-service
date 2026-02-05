package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.LandingComponent;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LandingComponentRepository extends JpaRepository<LandingComponent, Long> {

    List<LandingComponent> findBySection_IdAndSection_Page_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(Long sectionId, User owner);

    Optional<LandingComponent> findByIdAndSection_Page_Landing_OwnerAndDeletedFalse(Long id, User owner);

    @Modifying
    @Query("update LandingComponent c set c.deleted = true where c.section.id = :sectionId")
    int softDeleteBySectionId(@Param("sectionId") Long sectionId);

    @Modifying
    @Query("update LandingComponent c set c.deleted = true where c.section.page.id = :pageId")
    int softDeleteByPageId(@Param("pageId") Long pageId);
}
