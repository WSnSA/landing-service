package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.Subscription;
import mn.landing.landing_service.Entity.SubscriptionStatus;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findFirstByUserAndStatusAndEndAtAfterOrderByEndAtDesc(
            User user, SubscriptionStatus status, LocalDateTime now
    );

    Optional<Subscription> findFirstByUserAndDeletedFalseAndStatusAndEndAtAfterOrderByEndAtDesc(
            User user, SubscriptionStatus status, LocalDateTime now
    );
}
