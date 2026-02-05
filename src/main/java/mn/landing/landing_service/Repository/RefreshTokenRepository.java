package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.RefreshToken;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    long deleteByUser(User user);
}
