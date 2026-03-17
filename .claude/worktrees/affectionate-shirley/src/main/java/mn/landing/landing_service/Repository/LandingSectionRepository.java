package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.LandingSection;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LandingSectionRepository extends JpaRepository<LandingSection, Long> {

    List<LandingSection> findByPage_IdAndPage_Landing_OwnerAndDeletedFalseOrderByOrderIndexAsc(Long pageId, User owner);

    Optional<LandingSection> findByIdAndPage_Landing_OwnerAndDeletedFalse(Long id, User owner);

    @Modifying
    @Query("update LandingSection s set s.deleted = true where s.page.id = :pageId")
    int softDeleteByPageId(@Param("pageId") Long pageId);
}
