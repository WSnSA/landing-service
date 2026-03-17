package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByRegisterNumber(String registerNumber);
    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}

